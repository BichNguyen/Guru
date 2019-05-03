package com.getguru.interview.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EarthQuakeDataServiceTest {

  @InjectMocks private EarthquakeDataService earthquakeDataService;

  private Double LARGEST_MAGNITUDE_IN_TEST_JSON_FILE = Double.valueOf(5);
  public static int NUMBER_OF_OBJECTS_IN_TEST_JSON_FILE = 4;
  private Double RECURRING_MAGNITUDE = Double.valueOf(4.7);

  @Test
  public void testSortRawEarthquakeDataByDefaults_Success() throws IOException {
    // Set Up
    List<RawEarthquakeData> dataList = loadTestFile();

    // Method Call
    earthquakeDataService.sortRawEarthquakeDataByDefaults(
        dataList, earthquakeDataService.getDefaultComparator());

    // Verify
    Assert.assertEquals(NUMBER_OF_OBJECTS_IN_TEST_JSON_FILE, dataList.size());
    Assert.assertEquals(LARGEST_MAGNITUDE_IN_TEST_JSON_FILE, dataList.get(0).getMagnitude());
  }

  @Test
  public void testSortRawEarthquakeDataByDefaults_SameMag() throws IOException {
    // Set Up
    List<RawEarthquakeData> dataList = loadTestFile();

    // Method Call
    earthquakeDataService.sortRawEarthquakeDataByDefaults(
        dataList, earthquakeDataService.getDefaultComparator());

    // Verify
    List<RawEarthquakeData> filteredBySameMagList =
        dataList.stream()
            .filter(data -> data.getMagnitude().equals(RECURRING_MAGNITUDE))
            .collect(Collectors.toList());

    Assert.assertEquals(3, filteredBySameMagList.size());
    Assert.assertTrue(
        dateIsBefore(
            filteredBySameMagList.get(0).getTime(), filteredBySameMagList.get(1).getTime()));
    Assert.assertTrue(
        dateIsBefore(
            filteredBySameMagList.get(1).getTime(), filteredBySameMagList.get(2).getTime()));
  }

  public static List<RawEarthquakeData> loadTestFile() throws IOException {
    ClassLoader classLoader = EarthQuakeDataServiceTest.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream("earthquake_test.json");
    List<RawEarthquakeData> dataList =
        Arrays.asList(new ObjectMapper().readValue(is, RawEarthquakeData[].class));
    return dataList;
  }

  private boolean dateIsBefore(String dateToCheck, String dateToCompareAgainst) {
    return OffsetDateTime.parse(dateToCheck).isBefore(OffsetDateTime.parse(dateToCompareAgainst));
  }
}
