package ru.vizzi.Utils.shader.pbr;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.math.MathRotateHelper;
import ru.vizzi.Utils.shader.GameSettings;
import ru.vizzi.Utils.shader.KrogenitShaders;
import ru.vizzi.Utils.shader.ShaderLoader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class ForwardPBRDirectionalShaderOld extends AbstractPBRShader {
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

    private final Vector3f directColor = new Vector3f();
    private final Vector3f directDirection = new Vector3f();

    public ForwardPBRDirectionalShaderOld() {
        super(new ResourceLocation("shaders", "base_shader_old.vs"), new ResourceLocation("shaders", "forward_pbr_directional.fs"));
    }

    @Override
    public void bindAttribLocations() {
        glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_position, "in_position");
        glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_textureCoords, "in_textureCoords");
        glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_normal, "in_normal");
        glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_tangent, "in_tangent");
    }

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

    public void updateDirectionalLight(double delta) {
        directDirection.x = -100f;
        directDirection.y = 0;
        directDirection.z = 0f;

        float celestialAngle = mc.theWorld.getCelestialAngle(0);
        float angle = celestialAngle * -360.0F;
        float orig = angle;
        float rassvetStart = -270f;
        float dayStart = -278f;
        float dayEnd = -90f;

        float speed = (float) (delta * 0.005f);
        if(directColor.length() == 0) {
            speed = 1;
        }

        if (angle <= rassvetStart || angle > dayEnd) {
            if(orig <= dayStart || orig > dayEnd) {
                if(orig < - 78 && orig > -270) {
                    if(orig < -89) {
                        //zakat
                        float r = 0.0f;
                        float g = 0.0f;
                        float b = 0.0f;
                        updateDirectionalLightColor(r, g, b, speed);
                    } else {

                        //zakat
                        float r = 0.7f;
                        float g = 0.3f;
                        float b = 0.15f;
                        updateDirectionalLightColor(r, g, b, speed);
                    }
                } else {
                    //day
                    float r = 0.5f;
                    float g = 0.5f;
                    float b = 0.5f;
                    updateDirectionalLightColor(r, g, b, speed);
                }
            } else {
                //rassvet
                float r = 0.6f;
                float g = 0.3f;
                float b = 0.15f;
                updateDirectionalLightColor(r, g, b, speed);
            }

            //day
            if(angle > dayEnd) {
                //angle *= -1;
                angle -= dayEnd;
            }
            else {
                //angle *= -1f;
                angle -= 270;
            }

            MathRotateHelper.rotateAboutZ(directDirection, Math.toRadians(-angle));
        } else {
            //night
            float r = 0.2f;
            float g = 0.2f;
            float b = 0.8f;
            if(orig < -269) {
                r = 0.0f;
                g = 0.0f;
                b = 0.0f;
            }

            updateDirectionalLightColor(r, g, b, speed);
            //angle *= -1f;
            angle -= 90f;
            MathRotateHelper.rotateAboutZ(directDirection, Math.toRadians(-angle));
        }

        setDirectionLightColor(directColor.x, directColor.y, directColor.z);
        setDirectionLightDirection(directDirection.x, directDirection.y, directDirection.z);

        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
            KrogenitShaders.reload();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Перезагружен"));
        }
    }

    private void updateDirectionalLightColor(float r, float g, float b, float speed) {
        if(directColor.x < r) {
            directColor.x += speed;
            if(directColor.x > r) directColor.x = r;
        } else if(directColor.x > r) {
            directColor.x -= speed;
            if(directColor.x < r) directColor.x = r;
        }
        if(directColor.y < g) {
            directColor.y += speed;
            if(directColor.y > g) directColor.y = g;
        } else if(directColor.y > g) {
            directColor.y -= speed;
            if(directColor.y < g) directColor.y = g;
        }
        if(directColor.z < b) {
            directColor.z += speed;
            if(directColor.z > b) directColor.z = b;
        } else if(directColor.z > b) {
            directColor.z -= speed;
            if(directColor.z < b) directColor.z = b;
        }
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
            this.setMatrix(loc_modelView, matrix);
        }
    }

    public Vector3f getDirectDirection() {
        return directDirection;
    }

    public Vector3f getDirectColor() {
        return directColor;
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
            KrogenitShaders.forwardPBRDirectionalShader.enable();
            KrogenitShaders.forwardPBRDirectionalShader.setColor(r, g, b, a);
            KrogenitShaders.forwardPBRDirectionalShaderOld.enable();
            prevColor.x = r;
            prevColor.y = g;
            prevColor.z = b;
            prevColor.w = a;
        }
    }

    @Override
    public void setViewMatrix() {

    }

    @Override
    public void setLightMapCoords(float x, float y) {

    }

    @Override
    public void useLighting(boolean value) {
        setDirectionLight(value);
    }
}
