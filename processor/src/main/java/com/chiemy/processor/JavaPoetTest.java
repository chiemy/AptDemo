package com.chiemy.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.junit.Test;

import javax.lang.model.element.Modifier;

public class JavaPoetTest {

    @Test
    public void methodSpec() {

        MethodSpec method = MethodSpec.methodBuilder("total")
                .returns(int.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("int total = 0")
                .beginControlFlow("for(int i = 0 ; i < 10 ; i++)")
                .addStatement("total += i")
                .endControlFlow()
                .addStatement("return total")
                .build();
        System.out.println(method);
    }

    @Test
    public void typeSpec() {
        MethodSpec constructorMethod = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();

        // 属性
        FieldSpec fieldSpec = FieldSpec.builder(int.class, "TYPE")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                // 初始值
                .initializer("1")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("Bean")
                // 添加类限定符
                .addModifiers(Modifier.PUBLIC)
                // 添加泛型
                .addTypeVariable(TypeVariableName.get("T"))
                // 添加内部类
                .addType(TypeSpec.interfaceBuilder("Listener").build())
                // 添加成员变量
                .addField(String.class, "name", Modifier.PRIVATE)
                .addField(fieldSpec)
                // 添加方法
                .addMethod(constructorMethod)
                .build();

        System.out.println(typeSpec);
    }

    @Test
    public void testL() {
        MethodSpec spec = computeRange("plus", 1, 10, "+");
        System.out.println(spec);
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)
                .addStatement("result = result $L i", op)
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

}
