package com.julys.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils{



  public static void copyNonNullProperties(Object src, Object target){
    org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

  public static String[] getNullPropertyNames(Object source){
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);

    PropertyDescriptor[] propertyDescriptors = wrappedSource.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();

    for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
      Object propertyValue = wrappedSource.getPropertyValue(propertyDescriptor.getName());

      if(propertyValue == null){
        emptyNames.add(propertyDescriptor.getName());
      }
    }

    String[] result = new String[emptyNames.size()];

    return emptyNames.toArray(result);
  }
}