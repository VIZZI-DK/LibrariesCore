package ru.vizzi.Utils.shader.pbr;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.vizzi.Utils.shader.GL;
import ru.vizzi.Utils.shader.GameSettings;

import java.nio.FloatBuffer;

public class ForwardPBRDirectionalShader extends AbstractPBRShader {
    private int loc_useTexture;
    private int loc_textureDiffuse;
    private int loc_useLightMap;
    private int loc_textureLightMap;
    private int loc_textureNormal;
    private int loc_textureSpecular;
    private int loc_textureEmission;
    private int loc_textureGloss;
    private int loc_useNormalMapping;
    private int loc_useSpecularMapping;
    private int loc_useEmissionMapping;
    private int loc_emissionPower;
    private int loc_useGlossMapping;

    private int loc_modelView;

    private int loc_useDirectLight;
    private int loc_directLightColor;
    private int loc_directLightDirection;
    private int loc_directLightSpecular;

    private int loc_inColor;
    private int loc_projMat;
    private int loc_viewMat;
    private int loc_inLightMapCoord;
    private int loc_lightMapMatrix;

    public ForwardPBRDirectionalShader() {
        super(new ResourceLocation("shaders", "base_shader.vs"), new ResourceLocation("shaders", "forward_pbr_directional.fs"));
    }

    @Override
    public void bindAttribLocations() { }

    @Override
    protected void getAllUniformLocations() {
        loc_useTexture = super.getUniformLocation("useTexture");
        loc_textureDiffuse = super.getUniformLocation("diffuse");
        loc_useLightMap = super.getUniformLocation("useLightMap");
        loc_textureLightMap = super.getUniformLocation("lightMap");
        loc_textureNormal = super.getUniformLocation("normalMap");
        loc_textureSpecular = super.getUniformLocation("specularMap");
        loc_textureEmission = super.getUniformLocation("emissionMap");
        loc_textureGloss = super.getUniformLocation("glossMap");
        loc_useNormalMapping = super.getUniformLocation("useNormalMapping");
        loc_useSpecularMapping = super.getUniformLocation("useSpecularMapping");
        loc_useEmissionMapping = super.getUniformLocation("useEmissionMapping");
        loc_emissionPower = super.getUniformLocation("emissionPower");
        loc_useGlossMapping = super.getUniformLocation("useGlossMapping");

        loc_modelView = super.getUniformLocation("modelView");

        loc_useDirectLight = super.getUniformLocation("useDirectLight");
        loc_directLightColor = super.getUniformLocation("directLight[0].color");
        loc_directLightDirection = super.getUniformLocation("directLight[0].dir");
        loc_directLightSpecular = super.getUniformLocation("directLight[0].specular");

        loc_inColor = super.getUniformLocation("in_color");
        loc_projMat = super.getUniformLocation("projMat");
        loc_viewMat = super.getUniformLocation("viewMat");
        loc_inLightMapCoord = super.getUniformLocation("in_lightMapCoord");
        loc_lightMapMatrix = super.getUniformLocation("lightMapMatrix");
    }

    @Override
    protected void init() {
        this.setInt(loc_textureDiffuse, 0);
        this.setInt(loc_textureLightMap, 1);
        this.setInt(loc_textureNormal, 2);
        this.setInt(loc_textureSpecular, 3);
        this.setInt(loc_textureEmission, 4);
        setInt(loc_textureGloss, 5);
        setEmissionPower(1.0f);
        setSpecularPower(1.0f);
        setUseTexture(true);
        setLightMapping(true);
        setColor(1f, 1f, 1f, 1f);
        setMatrix(loc_lightMapMatrix, GL.getLightMapTextureMatrix());
    }

    @Override
    public void setUseTexture(boolean value) {
        if(prevUseTexture != value) {
            setBoolean(loc_useTexture, value);
            prevUseTexture = value;
        }
    }


    @Override
    public void setLightMapping(boolean value) {
        if(prevLightMapping != value) {
            setBoolean(loc_useLightMap, value);
            prevLightMapping = value;
        }
    }

    public void setSpecularPower(float value) {
        setFloat(loc_directLightSpecular, value);
    }


    public void setDirectionLight(boolean value) {
        if(GameSettings.lighting > 0) {
            if(prevLight != value) {
                setBoolean(loc_useDirectLight, value);
                prevLight = value;
            }
        }
    }


    public void setDirectionLightColor(float r, float g, float b) {
        if(GameSettings.lighting > 0) {
            if(prevDirColor.x != r || prevDirColor.y != g || prevDirColor.z != b) {
                setVector(loc_directLightColor, r, g, b);
                prevDirColor.x = r;
                prevDirColor.y = g;
                prevDirColor.z = b;
            }
        }
    }


    public void setDirectionLightDirection(float x, float y, float z) {
        if(GameSettings.lighting > 0) {
            if(prevDir.x != x || prevDir.y != y || prevDir.z != z) {
                setVector(loc_directLightDirection, x, y, z);
                prevDir.x = x;
                prevDir.y = y;
                prevDir.z = z;
            }
        }
    }

    @Override
    public void setNormalMapping(boolean value) {
        if(GameSettings.normalMapping) {
            if(prevNormalMapping != value) {
                setBoolean(loc_useNormalMapping, value);
                prevNormalMapping = value;
            }
        }
    }

    @Override
    public void setEmissionMapping(boolean value) {
        if(GameSettings.emissionMapping) {
            if(prevEmissionMapping != value) {
                setBoolean(loc_useEmissionMapping, value);
                prevEmissionMapping = value;
            }
        }
    }

    @Override
    public void setSpecularMapping(boolean value) {
        if(GameSettings.specularMapping) {
            if(prevSpecularMapping != value) {
                setBoolean(loc_useSpecularMapping, value);
                prevSpecularMapping = value;
            }
        }
    }

    @Override
    public void setEmissionPower(float value) {
        if(GameSettings.emissionMapping) {
            if(prevEmissionPower != value) {
                setFloat(loc_emissionPower, value);
                prevEmissionPower = value;
            }
        }
    }

    @Override
    public void setGlossMapping(boolean value) {
        if(GameSettings.glossMapping) {
            if(prevGlossMapping != value) {
                setBoolean(loc_useGlossMapping, value);
                prevGlossMapping = value;
            }
        }
    }

    public void setModelView() {
        if(GameSettings.lighting > 0) {
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
            setMatrix(loc_modelView, matrix);
        }
    }

    @Override
    public void setLightColor(float r, float g, float b) {
        this.setDirectionLightColor(r, g, b);
    }

    @Override
    public void setLightPos(float x, float y, float z) {
        this.setDirectionLightDirection(x, y, z);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        if(prevColor.x != r || prevColor.y != g || prevColor.z != b || prevColor.w != a) {
            setVector(loc_inColor, r, g, b, a);
            prevColor.x = r;
            prevColor.y = g;
            prevColor.z = b;
            prevColor.w = a;
        }
    }

    public void setProjectionMatrix() {
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_projMat, matrix);
    }

    @Override
    public void setViewMatrix() {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_viewMat, matrix);
    }

    @Override
    public void setLightMapCoords(float x, float y) {
        if(prevLightMapCoords.x != x || prevLightMapCoords.y != y) {
            this.setVector(loc_inLightMapCoord, x, y);
            prevLightMapCoords.x = x;
            prevLightMapCoords.y = y;
        }
    }

    @Override
    public void useLighting(boolean value) {
        setDirectionLight(value);
    }
}
