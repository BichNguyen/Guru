package com.getguru.interview.resources;

import com.getguru.interview.db.EarthQuakeDataServiceTest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataResourceTest {

  @InjectMocks private DataResource dataResource;

  @Test
  public void testCreatePredicate_FilterNotNull() {
    // Set Up
    Optional<String> opt = Optional.of("NW");

    // Method Call
    Predicate<Earthquake> earthquakePredicate =
        dataResource.getPlaceFilterPredicateFromOptional(opt);

    // Verify
    Assert.assertTrue(earthquakePredicate.test(getDefaultEarthquakeObject()));
  }

  @Test
  public void testCreatePredicate_FilterIsEmptyString() {
    // Set Up
    Optional<String> opt = Optional.of("");

    // Method Call
    Boolean isValidFilter = dataResource.isFilterValid(opt);

    // Verify
    Assert.assertFalse(isValidFilter);
  }

  @Test
  public void testCreatePredicate_FilterIsSpace() {
    // Set Up
    Optional<String> opt = Optional.of(" ");

    // Method Call
    Boolean isValidFilter = dataResource.isFilterValid(opt);

    // Verify
    Assert.assertFalse(isValidFilter);
  }

  @Test
  public void testConvertToEarthQuakeDataObject_OptionalFilterIsNotValid() throws IOException {
    // Set Up
    String filterValue = "";

    // Method Call
    List<Earthquake> returnedObjects =
        dataResource.convertToEarthQuakeDataObjectList(
            EarthQuakeDataServiceTest.loadTestFile(), Optional.ofNullable(filterValue));

    // Verify
    Assert.assertEquals(
        EarthQuakeDataServiceTest.NUMBER_OF_OBJECTS_IN_TEST_JSON_FILE, returnedObjects.size());
  }

  @Test
  public void testConvertToEarthQuakeDataObject_OptionalFilterIsValid() throws IOException {
    // Set Up
    String filterValue = "SW";

    // Method Call
    List<Earthquake> returnedObjects =
        dataResource.convertToEarthQuakeDataObjectList(
            EarthQuakeDataServiceTest.loadTestFile(), Optional.ofNullable(filterValue));

    // Verify
    Assert.assertEquals(1, returnedObjects.size());
  }

  protected Earthquake getDefaultEarthquakeObject() {
    Earthquake earthquake = new Earthquake();
    earthquake.setPlace("NW Of something xyz");
    return earthquake;
  }
}
