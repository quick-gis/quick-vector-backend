package com.github.quickvectorbackend.utils;

import static org.geotools.data.Transaction.AUTO_COMMIT;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://github.com/yieryi/gts4vect
 */
public class Geotools {

    public static void main(String[] args) {
        Geotools geotools = new Geotools();
        geotools.geojson2shp(
            "/Users/zhangsan/youkong/quick-vector-backend/src/main/resources/line-ring.geojson",
            "/Users/zhangsan/youkong/quick-vector-backend/src/main/resources/static/a.shp");
    }
    private static final Logger logger = LoggerFactory.getLogger(Geotools.class);

    /**
     * featureCollection写入到shp的datastore
     *
     * @param featureCollection
     * @param shpDataStore
     * @param geomFieldName     featureCollectio中的矢量字段，postgis可以修改使用不同的表名，默认为geom
     * @return
     */
    public static boolean write2shp(FeatureCollection featureCollection,
        ShapefileDataStore shpDataStore, String geomFieldName) {
        boolean result = false;
        if (Utility.isEmpty(geomFieldName)) {
            geomFieldName = featureCollection.getSchema().getGeometryDescriptor().getType()
                .getName().toString();
        }
        try {
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            //shp文件存储写入
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shpDataStore.getFeatureWriter(
                shpDataStore.getTypeNames()[0], AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                Iterator<Property> propertyIterator = properties.iterator();

                while (propertyIterator.hasNext()) {
                    Property property = propertyIterator.next();
                    if (property.getName().toString().equalsIgnoreCase(geomFieldName)) {
                        simpleFeature.setAttribute("the_geom", property.getValue());
                    } else {
                        simpleFeature.setAttribute(property.getName().toString(),
                            property.getValue());
                    }
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            shpDataStore.dispose();
        } catch (Exception e) {
            logger.error("失败", e);
        }
        return false;
    }

    /**
     * 方法重载，默认使用UTF-8的Shp文件
     *
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public boolean geojson2shp(String geojsonPath, String shpfilepath) {
        return geojson2shp(geojsonPath, shpfilepath, ShpCharset.UTF_8);
    }

    /**
     * Geojson转成shpfile文件
     *
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public boolean geojson2shp(String geojsonPath, String shpfilepath, Charset shpChart) {
        boolean result = false;
        try {
            FeatureJSON featureJSON = new FeatureJSON();
            featureJSON.setEncodeNullValues(true);
            FeatureCollection featureCollection = featureJSON.readFeatureCollection(
                new InputStreamReader(new FileInputStream(geojsonPath), StandardCharsets.UTF_8));

            File file = new File(shpfilepath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(
                params);

            //postgis获取的Featuretype获取坐标系代码
            SimpleFeatureType pgfeaturetype = (SimpleFeatureType) featureCollection.getSchema();

            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(pgfeaturetype);
            typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
            pgfeaturetype = typeBuilder.buildFeatureType();
            //设置成utf-8编码
            shpDataStore.setCharset(shpChart);
            shpDataStore.createSchema(pgfeaturetype);
            write2shp(featureCollection, shpDataStore, "");
            result = true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 重载方法，默认UTF-8编码SHP文件
     *
     * @param shpPath
     * @param geojsonPath
     * @return
     */
    public boolean shp2geojson(String shpPath, String geojsonPath) {
        return shp2geojson(shpPath, geojsonPath, ShpCharset.UTF_8);
    }

    /**
     * shp转成geojson，保留15位小数
     *
     * @param shpPath     shp的路径
     * @param geojsonPath geojson的路径
     * @return
     */
    public boolean shp2geojson(String shpPath, String geojsonPath, Charset shpCharset) {
        boolean result = false;
        try {
            if (!Utility.valiFileForRead(shpPath) || Utility.isEmpty(geojsonPath)) {
                return result;
            }
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(
                new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            ContentFeatureSource featureSource = shapefileDataStore.getFeatureSource();
            ContentFeatureCollection contentFeatureCollection = featureSource.getFeatures();
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            Utility.valiFileForWrite(geojsonPath);
            featureJSON.writeFeatureCollection(contentFeatureCollection, new File(geojsonPath));
            shapefileDataStore.dispose();
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }






}
