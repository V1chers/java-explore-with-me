package ru.practicum.ewm.statistics.server.model;

public interface GetStats {

    App getApp();

    String getUriPath();

    Integer getUriId();

    Integer getTotalViews();
}
