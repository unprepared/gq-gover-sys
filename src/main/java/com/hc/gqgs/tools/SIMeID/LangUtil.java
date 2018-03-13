package com.hc.gqgs.tools.SIMeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangUtil
{
  private static Logger logger = LoggerFactory.getLogger(LangUtil.class);
  
  public static Boolean parseBoolean(Object value)
  {
    if (value != null)
    {
      if ((value instanceof Boolean)) {
        return (Boolean)value;
      }
      if ((value instanceof String)) {
        return Boolean.valueOf((String)value);
      }
    }
    return null;
  }
  
  public static boolean parseBoolean(Object value, boolean defaultValue)
  {
    if (value != null)
    {
      if ((value instanceof Boolean)) {
        return ((Boolean)value).booleanValue();
      }
      if ((value instanceof String)) {
        try
        {
          return Boolean.valueOf((String)value).booleanValue();
        }
        catch (Exception e)
        {
          logger.warn("parse boolean value({}) failed.", value);
        }
      }
    }
    return defaultValue;
  }
  
  public static Integer parseInt(Object value)
  {
    if (value != null)
    {
      if ((value instanceof Integer)) {
        return (Integer)value;
      }
      if ((value instanceof String)) {
        return Integer.valueOf((String)value);
      }
    }
    return null;
  }
  
  public static Integer parseInt(Object value, Integer defaultValue)
  {
    if (value != null)
    {
      if ((value instanceof Integer)) {
        return (Integer)value;
      }
      if ((value instanceof String)) {
        try
        {
          return Integer.valueOf((String)value);
        }
        catch (Exception e)
        {
          logger.warn("parse Integer value({}) failed.", value);
        }
      }
    }
    return defaultValue;
  }
  
  public static Long parseLong(Object value)
  {
    if (value != null)
    {
      if ((value instanceof Long)) {
        return (Long)value;
      }
      if ((value instanceof String)) {
        return Long.valueOf((String)value);
      }
    }
    return null;
  }
  
  public static Long parseLong(Object value, Long defaultValue)
  {
    if (value != null)
    {
      if ((value instanceof Long)) {
        return (Long)value;
      }
      if ((value instanceof String)) {
        try
        {
          return Long.valueOf((String)value);
        }
        catch (NumberFormatException e)
        {
          logger.warn("parse Long value({}) failed.", value);
        }
      }
    }
    return defaultValue;
  }
  
  public static Double parseDouble(Object value)
  {
    if (value != null)
    {
      if ((value instanceof Double)) {
        return (Double)value;
      }
      if ((value instanceof String)) {
        return Double.valueOf((String)value);
      }
    }
    return null;
  }
  
  public static Double parseDouble(Object value, Double defaultValue)
  {
    if (value != null)
    {
      if ((value instanceof Double)) {
        return (Double)value;
      }
      if ((value instanceof String)) {
        try
        {
          return Double.valueOf((String)value);
        }
        catch (NumberFormatException e)
        {
          logger.warn("parse Double value({}) failed.", value);
        }
      }
    }
    return defaultValue;
  }
}
