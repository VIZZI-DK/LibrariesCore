package ru.vizzi.Utils.shader.pbr;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import ru.vizzi.Utils.shader.ShaderProgram;

public abstract class AbstractPBRShader extends ShaderProgram implements IPBR {

    protected boolean prevUseTexture;
    protected boolean prevLightMapping;
    protected boolean prevLight;
    protected final Vector3f prevDirColor = new Vector3f();
    protected final Vector3f prevDir = new Vector3f();
    protected boolean prevNormalMapping;
    protected boolean prevSpecularMapping;
    protected boolean prevGlossMapping;
    protected boolean prevEmissionMapping;
    protected float prevEmissionPower;
    protected final Vector4f prevColor = new Vector4f();
    protected final Vector2f prevLightMapCoords = new Vector2f();

    public AbstractPBRShader(ResourceLocation resVs, ResourceLocation resFs) {
        super(resVs, resFs);
    }
}
