package com.sbsc.convertee.entities.unittypes.generic;

public class RawUnit {

    private final String key;
    private final String factor;
    private final String resourceId;
    private final String sampleInput;

    public RawUnit(String key, String resourceId, String factor, String sampleInput) {
        this.key = key;
        this.resourceId = resourceId;
        this.factor = factor;
        this.sampleInput = sampleInput;
    }

    public String getKey() { return key; }
    public String getFactor() { return factor; }
    public String getResourceId() { return resourceId; }
    public String getSampleInput() { return sampleInput; }
}
