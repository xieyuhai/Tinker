package com.xyh.arouterannotationcompile;

import com.google.auto.service.AutoService;
import com.xyh.annotation.BindPath;
import com.xyh.utils.CloseUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;


@AutoService(Processor.class)// 注册注解处理器
public class AnnotationCompiler extends AbstractProcessor {

    //生成文件对象类

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> annotations = new HashSet<>();

        annotations.add(BindPath.class.getCanonicalName());

        return annotations;
    }


    /**
     * 支持java的版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //得到所有的类节点
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
//        创建一个集合，
        Map<String, String> map = new HashMap<>();

        //遍历 得到每一个Activity类节点
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            //得到包名加类名
            String activityName = typeElement.getQualifiedName().toString();
            String key = typeElement.getAnnotation(BindPath.class).value();

            map.put(key, activityName + ".class");
        }

        if (!map.isEmpty()) {
            //写入文件 拼接 生成类
            Writer writer = null;
            String utilName = "ActivityUtils_" + System.currentTimeMillis();

            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.xyh.utils." + utilName);
                writer = sourceFile.openWriter();

                writer.write("package com.xyh.utils;\n" +
                                "import com.xyh.arouter.ARouter;\n" +
                                "import com.xyh.arouter.IRouter; \n" +
                                "public class " + utilName + " implements IRouter {\n" +
                                "    @Override\n" +
                                "    public void putActivity() {\n"
//                        "        ARouter.getInstance().navigation(\"member\", null);\n" +
                );
                System.out.println("==================");
                System.out.println("==================");
                System.out.println("==================");
                Iterator<String> iterator = map.keySet().iterator();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String activityName = map.get(key);

                    writer.write("ARouter.getInstance().addActivity(\"" + key + "\"," + activityName + "); \n");
                }

                writer.append("}\n}");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.close(writer);
            }
        }
        return false;
    }
}
