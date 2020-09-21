package com.iot.user.cluster.clustering;

import com.amap.api.maps.model.LatLng;

import java.util.Collection;

/**
 * A collection of ClusterItems that are nearby each other.
 */
public interface Cluster<T extends ClusterItem> {
    LatLng getPosition();

    Collection<T> getItems();

    int getSize();
}