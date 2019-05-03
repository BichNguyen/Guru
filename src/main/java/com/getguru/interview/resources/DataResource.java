package com.getguru.interview.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getguru.interview.db.EarthquakeDataService;

import com.getguru.interview.db.RawEarthquakeData;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {

  @SuppressWarnings("unused")
  private EarthquakeDataService _dataService = new EarthquakeDataService(new ObjectMapper());

  @GET
  public List<Earthquake> getEarthquakes(@QueryParam("filter") Optional<String> filter)
      throws IOException {
    List<RawEarthquakeData> rawEarthquakeData = _dataService.getEarthquakeData();
    return convertToEarthQuakeDataObjectList(rawEarthquakeData, filter);
  }

  /**
   * Based on the passed in filter will build a predicate to use for filtering out objects
   *
   * @param filter
   * @return
   */
  protected Predicate<Earthquake> getPlaceFilterPredicateFromOptional(Optional<String> filter) {
    return earthquakeData -> earthquakeData.getPlace().contains(filter.get());
  }

  /**
   * Takes a list of RawEarthquakeData and transforms it to the Api friendly Api Object
   *
   * @param rawEarthquakeData
   * @param filter
   * @return
   */
  protected List<Earthquake> convertToEarthQuakeDataObjectList(
      List<RawEarthquakeData> rawEarthquakeData, Optional<String> filter) {
    Predicate<Earthquake> optionalFilter = getPlaceFilterPredicateFromOptional(filter);
    return rawEarthquakeData.stream()
        .map(
            data -> {
              return new Earthquake.Builder(data).build();
            })
        .filter(i -> !isFilterValid(filter) || getPlaceFilterPredicateFromOptional(filter).test(i))
        .collect(Collectors.toList());
  }

  /**
   * Simple check to ensure the filter is valid ... prevent empty strings from being passed in that
   * could render the filtering aspect inefficient
   *
   * @param filter
   * @return
   */
  protected boolean isFilterValid(Optional<String> filter) {
    return filter.isPresent() && !StringUtils.isEmpty(filter.get().trim());
  }
}
