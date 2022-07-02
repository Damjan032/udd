package com.ftn.udd.services;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

@Service
public interface GeoPointService {

	GeoPoint getGeoPoint(String city, String country) throws Exception;

	String getCityByCoords(double lat, double lon) throws Exception;
}
