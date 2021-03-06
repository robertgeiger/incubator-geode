package com.gemstone.gemfire.cache.query.internal.utils;

import com.gemstone.gemfire.cache.CacheException;
import com.gemstone.gemfire.cache.query.internal.StructImpl;
import com.gemstone.gemfire.internal.cache.VMCachedDeserializable;
import com.gemstone.gemfire.pdx.PdxInstance;
import com.gemstone.gemfire.pdx.internal.PdxString;

public class PDXUtils {

  public static Object convertPDX(Object obj, boolean isStruct,
      boolean getDomainObjectForPdx, boolean getDeserializedObject,
      boolean localResults, boolean[] objectChangedMarker, boolean isDistinct) {
    objectChangedMarker[0] = false;
    if (isStruct) {
      StructImpl simpl = (StructImpl) obj;
      if (getDomainObjectForPdx) {
        try {
          if (simpl.isHasPdx()) {
            obj = simpl.getPdxFieldValues();
            objectChangedMarker[0] = true;
          } else {
            obj = simpl.getFieldValues();
          }
        } catch (Exception ex) {
          throw new CacheException(
              "Unable to retrieve domain object from PdxInstance while building the ResultSet. "
                  + ex.getMessage()) {
          };
        }
      } else {
        Object[] values = simpl.getFieldValues();
        if (getDeserializedObject) {
          for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof VMCachedDeserializable) {
              values[i] = ((VMCachedDeserializable) values[i])
                  .getDeserializedForReading();
            }
          }
        }
        /* This is to convert PdxString to String */
        if (simpl.isHasPdx() && isDistinct && localResults) {
          for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof PdxString) {
              values[i] = ((PdxString) values[i]).toString();
            }
          }
        }
        obj = values;
      }
    } else {
      if (getDomainObjectForPdx) {
        if (obj instanceof PdxInstance) {
          try {
            obj = ((PdxInstance) obj).getObject();
            objectChangedMarker[0] = true;
          } catch (Exception ex) {
            throw new CacheException(
                "Unable to retrieve domain object from PdxInstance while building the ResultSet. "
                    + ex.getMessage()) {
            };
          }
        } else if (obj instanceof PdxString) {
          obj = ((PdxString) obj).toString();
        }
      } else if (isDistinct && localResults && obj instanceof PdxString) {
        /* This is to convert PdxString to String */
        obj = ((PdxString) obj).toString();
      }

      if (getDeserializedObject && obj instanceof VMCachedDeserializable) {
        obj = ((VMCachedDeserializable) obj).getDeserializedForReading();
        objectChangedMarker[0] = true;
      }

    }
    return obj;
  }  
}
