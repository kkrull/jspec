package org.jspec;

import static org.junit.Assert.*;

import static org.junit.Assume.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class ReflectionUtilTests {
  public class fieldsOfType {
    @Test
    public void givenClassWithNoMatchingFields_returnsEmptyList() {
      assertFieldsOfType(MatchingType.class, EmptyClass.class);
      assertFieldsOfType(MatchingType.class, EmptyNestedClass.class);
    }
    
    @Test
    public void givenClassWith1OrMoreMatchingFields_returnsEachField() {
      assertFieldsOfType(MatchingType.class, MixedFields.class, "matching");
    }
    
    @Test
    public void givenAnyFieldVisibility_returnsTheField() {
      assertFieldsOfType(MatchingType.class, FieldVisibility.class,
        "publicField", "protectedField", "defaultVisibilityField", "privateField");
    }
    
    @Test
    public void givenAClassWithInheritedFields_excludesThoseFields() {
      MatchingType value = new MatchingType();
      assumeTrue("The super type's field should be inherited", new SubType(value).superTypeField == value);
      assertFieldsOfType(MatchingType.class, SubType.class, "subTypeField");
    }
    
    void assertFieldsOfType(Class<?> fieldType, Class<?> typeToInspect, String... names) {
      final Comparator<String> alphabetical = (x, y) -> x.compareTo(y);
      List<Field> fields = ReflectionUtil.fieldsOfType(fieldType, typeToInspect).collect(Collectors.toList());
      List<String> actualNames = fields.stream().map(Field::getName).sorted(alphabetical).collect(Collectors.toList());
      List<String> expectedNames = Arrays.asList(names).stream().sorted(alphabetical).collect(Collectors.toList());
      assertEquals(expectedNames, actualNames);
    }
    
    class EmptyNestedClass { /* Not devoid of fields; there is 1 pointing to the containing class */ }
    
    class MixedFields {
      public MatchingType matching;
      public NonMatchingType nonMatching;
    }
    
    class FieldVisibility {
      public MatchingType publicField;
      protected MatchingType protectedField;
      MatchingType defaultVisibilityField;
      
      @SuppressWarnings("unused")
      private MatchingType privateField;
    }
    
    class SubType extends SuperType {
      public MatchingType subTypeField;
      public SubType(MatchingType superTypeField) {
        this.superTypeField = superTypeField;
      }
    }
    
    class SuperType {
      public MatchingType superTypeField;
    }
    
    class MatchingType {}
    class NonMatchingType {}
  }
}