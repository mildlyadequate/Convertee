package com.sbsc.convertee.entities.unittypes.generic;

public class RawUnit {

    private String key;
    private String factor;
    private String resourceId;
    private String sampleInput;

    public RawUnit(String key, String resourceId, String factor, String sampleInput) {
        this.key = key;
        this.resourceId = resourceId;
        this.factor = factor;
        this.sampleInput = sampleInput;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getFactor() { return factor; }
    public void setFactor(String factor) { this.factor = factor; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public String getSampleInput() { return sampleInput; }
    public void setSampleInput(String sampleInput) { this.sampleInput = sampleInput; }
}
