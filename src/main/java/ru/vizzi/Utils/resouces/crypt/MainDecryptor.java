package ru.vizzi.Utils.resouces.crypt;

import javassist.CtClass;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import javassist.ClassPool;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Base64;

public class MainDecryptor implements IDecryptor{


    private static class ClassLoader extends java.lang.ClassLoader {
        public void defineClassBytes(String fullClassName, byte[] bytes) {
            defineClass(fullClassName, bytes, 0, bytes.length);
        }
    }


    private Object dcd;
    private Method method;

    public MainDecryptor(){
        try {
//            ClassPool classPool = ClassPool.getDefault();
//
//            // Загрузка существующего класса
//            CtClass ctClass = classPool.get("ru.vizzi.Utils.resouces.crypt.Test");
//
//            ;
//
//            // Получение байтов измененного класса
//            byte[] classBytes = ctClass.toBytecode();
//
//
//            Files.write(new File("MyClass.test2").toPath(), classBytes);



            byte[] bytes =  Files.readAllBytes(new File("MyClassendoce.test2").toPath());
          //  byte[] encode = Base64.getEncoder().encode(bytes);

          //  Files.write(new File("MyClassendoce.test2").toPath(), encode);


            byte[] decode = Base64.getDecoder().decode(bytes);

            ClassLoader classLoader = new ClassLoader();

            classLoader.defineClassBytes("ru.vizzi.Utils.resouces.crypt.Test", decode);
            dcd = classLoader.loadClass("ru.vizzi.Utils.resouces.crypt.Test").newInstance();
            method = dcd.getClass().getMethod("getDecryptedInputStream", InputStream.class);

            method.invoke(dcd, System.in);

            System.out.println("test");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("test");
        }





    }

    public static IDecryptor deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        IDecryptor test = (IDecryptor) objectInputStream.readObject();
        objectInputStream.close();
        return test;
    }

    private static byte[] writeClassBytes(String className) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, Type.getInternalName(Object.class), null);

        // Добавляем конструктор по умолчанию
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }


    private InputStream getEncryptedInputStream0(InputStream inputStream) throws Throwable {
        return (InputStream) method.invoke(dcd, inputStream);
    }


    @Override
    public InputStream getDecryptedInputStream(InputStream inputStream) throws Throwable {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int numberOfBytedRead;
            InputStream is = getEncryptedInputStream0(inputStream);
            while ((numberOfBytedRead = is.read(b)) >= 0) {
                baos.write(b, 0, numberOfBytedRead);
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            return byteArrayInputStream;
        }
    }


}
