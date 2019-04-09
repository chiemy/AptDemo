package com.chiemy.processor;

import com.chiemy.nbrouter.annotation.Route;
import com.google.auto.service.AutoService;
import com.google.common.reflect.TypeToken;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {
    private Map<String, String> routesInfo;

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        routesInfo = new HashMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new HashSet<>();
        supportedTypes.add(Route.class.getCanonicalName());
        return supportedTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Route.class)) {
            // 只能处理对类进行的注解
            if (element.getKind() != ElementKind.CLASS) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "只能对类进行注解");
                return true;
            }
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Route route = element.getAnnotation(Route.class);
                routesInfo.put(route.path(), typeElement.getQualifiedName().toString());
            }
        }

        if (roundEnv.processingOver()) {
            try {
                generateRoutes();
                return true;
            } catch (IOException ex) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, ex.toString());
            }
        }
        return false;
    }

    private void generateRoutes() throws IOException {
        final TypeSpec.Builder builder = TypeSpec.classBuilder("Routes")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ClassName.get("com.chiemy.lib.router", "IRoutes"))
                .addMethod(generateMethod());

        JavaFile.builder("com.chiemy.lib.router", builder.build())
                .build()
                .writeTo(mFiler);
    }

    private MethodSpec generateMethod() {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getRouteInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(new TypeToken<Map<String, String>>(){}.getType());

        ClassName map = ClassName.get("java.util", "Map");
        // Map<String, String>
        TypeName mapRoutes = ParameterizedTypeName.get(map, TypeName.get(String.class), TypeName.get(String.class));
        // Map<String, String> map = new HashMap<>();
        methodSpecBuilder.addStatement("$T map = new $T<>()", mapRoutes, HashMap.class);
        // map.put
        for (Map.Entry<String, String> entry : routesInfo.entrySet()) {
            methodSpecBuilder.addStatement("map.put($S, $S)", entry.getKey(), entry.getValue());
        }
        methodSpecBuilder.addStatement("return map");
        return methodSpecBuilder.build();
    }
}
