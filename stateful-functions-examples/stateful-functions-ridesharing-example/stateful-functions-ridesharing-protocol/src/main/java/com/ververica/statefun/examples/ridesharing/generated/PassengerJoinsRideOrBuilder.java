// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/protobuf/ridesharing.proto

package com.ververica.statefun.examples.ridesharing.generated;

public interface PassengerJoinsRideOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:com.ververica.statefun.examples.ridesharing.PassengerJoinsRide)
    com.google.protobuf.MessageOrBuilder {

  /** <code>string passenger_id = 1;</code> */
  java.lang.String getPassengerId();
  /** <code>string passenger_id = 1;</code> */
  com.google.protobuf.ByteString getPassengerIdBytes();

  /** <code>int32 start_geo_cell = 2;</code> */
  int getStartGeoCell();

  /** <code>int32 end_geo_cell = 3;</code> */
  int getEndGeoCell();
}
