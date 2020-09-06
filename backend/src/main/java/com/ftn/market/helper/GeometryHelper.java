package com.ftn.market.helper;

public final class GeometryHelper {

  public static final double R = 6372800D; // In meters

  public static double distance(final double lat1, final double lon1, final double lat2, final double lon2) {
    final double theta = lon1 - lon2;
    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
        + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    return dist * 1.609344;
  }

  private static double deg2rad(final double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(final double rad) {
    return (rad * 180.0 / Math.PI);
  }

  /**
   * The distance between the p1 and p2 in meters.
   *
   * @param lat1
   *          latitude of first point
   * @param lon1
   *          longitude of first point
   * @param lat2
   *          latitude of second point
   * @param lon2
   *          longitude of second point
   * @return The distance in meters
   */
  // public static double distance(Double lat1, final Double lon1, Double lat2, final Double lon2) {
  // final double dLat = Math.toRadians(lat2 - lat1);
  // final double dLon = Math.toRadians(lon2 - lon1);
  // lat1 = Math.toRadians(lat1);
  // lat2 = Math.toRadians(lat2);
  //
  // final double a =
  // Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
  // final double c = 2 * Math.asin(Math.sqrt(a));
  // return R * c;
  // }

  private GeometryHelper() {
    super();
  }
}
