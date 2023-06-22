package ru.vizzi.Utils.shader.pbr;

public interface IPBR {
    void setUseTexture(boolean value);
    void setLightMapping(boolean value);
    void setNormalMapping(boolean value);
    void setSpecularMapping(boolean value);
    void setEmissionMapping(boolean value);
    void setEmissionPower(float value);
    void setGlossMapping(boolean value);
    void setLightColor(float r, float g, float b);
    void setLightPos(float x, float y, float z);
    void setColor(float r, float g, float b, float a);
    void setViewMatrix();
    void setLightMapCoords(float x, float y);
    void useLighting(boolean value);
}
