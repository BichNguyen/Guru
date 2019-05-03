package com.getguru.interview.db;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EarthquakeDataService {
  @SuppressWarnings("unused")
  private ObjectMapper _objectMapper;

  public EarthquakeDataService(ObjectMapper mapper) {
    this._objectMapper = mapper;
  }

  private List<RawEarthquakeData> rawEarthquakeData;

  /**
   * Returns raw earthquake data loaded from a specific file.
   *
   * <p>To prevent having to load the json file on every call ... store it in a local variable. I
   * did this based on the assumption that the file contents itself wont really change.
   *
   * @return
   * @throws IOException
   */
  public List<RawEarthquakeData> getEarthquakeData() throws IOException {

    if (rawEarthquakeData == null) {
      rawEarthquakeData = loadRawEarthquakeData("earthquake.json");
      sortRawEarthquakeDataByDefaults(rawEarthquakeData, getDefaultComparator());
    }

    return rawEarthquakeData;
  }

  /**
   * Loads raw earthquake data from a given resource.
   *
   * @return
   * @throws IOException
   */
  protected List<RawEarthquakeData> loadRawEarthquakeData(String resourceFileName)
      throws IOException {

    InputStream is = getClass().getResourceAsStream(resourceFileName);
    return Arrays.asList(_objectMapper.readValue(is, RawEarthquakeData[].class));
  }

  /**
   * Sorts the data in the default order specified.
   *
   * <p>It seemed more efficient to sort only one time... that being the initial loading of the
   * data. After that we are only optionally filtering on the sorted data which can be done
   * dynamically with each call to the service.
   *
   * @param unsortedData
   * @param comparator
   */
  protected void sortRawEarthquakeDataByDefaults(
      List<RawEarthquakeData> unsortedData, Comparator<RawEarthquakeData> comparator) {
    Collections.sort(unsortedData, comparator);
  }

  /**
   * Comparator to sort data by default sort orders.
   *
   * @return
   */
  protected Comparator<RawEarthquakeData> getDefaultComparator() {

    return (o1, o2) -> {
      int returnValue = o2.getMagnitude().compareTo(o1.getMagnitude());

      if (returnValue != 0) {
        return returnValue;
      }

      return OffsetDateTime.parse(o1.getTime()).compareTo(OffsetDateTime.parse(o2.getTime()));
    };
  }
}
