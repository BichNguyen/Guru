package com.getguru.interview;

import com.getguru.interview.resources.DataResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class InterviewApplication extends Application<InterviewConfiguration> {

  public static void main(final String[] args) throws Exception {
    new InterviewApplication().run(args);
  }

  @Override
  public String getName() {
    return "interviewAssignment";
  }

  @Override
  public void initialize(final Bootstrap<InterviewConfiguration> bootstrap) {
    bootstrap.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    bootstrap.getObjectMapper().setDateFormat(new ISO8601DateFormat());
  }

  @Override
  public void run(final InterviewConfiguration configuration, final Environment environment) {
    final DataResource resource = new DataResource();
    environment.jersey().register(resource);
  }
}
