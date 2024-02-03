package ch.andrewrembrandt.effectiveit.config;

import ch.andrewrembrandt.effectiveit.model.TenantableEntity;
import java.text.MessageFormat;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription.Generic.Builder;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class DynamicR2dbcPersistersWithTenant implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    tenantableEntities()
        .forEach(
            te -> {
              val className =
                  MessageFormat.format(
                      "{0}.{1}R2dbcPersisterCallbackWithTenant",
                      getClass().getPackageName(), te.getSimpleName());

              Class<?> loadedClass;
              try {
                val alreadyLoadedClass = Class.forName(className);
                log.info("Class: {} already loaded", alreadyLoadedClass);
                loadedClass = alreadyLoadedClass;
              } catch (ClassNotFoundException e) {
                val bbLoadedClass = createClassForEntity(className, te);
                loadedClass = bbLoadedClass.getLoaded();
                log.info("Created & loaded class: {}", loadedClass.getName());
              }

              val bdb = BeanDefinitionBuilder.rootBeanDefinition(loadedClass);
              ((DefaultListableBeanFactory) beanFactory)
                  .registerBeanDefinition(className, bdb.getBeanDefinition());

              log.info("Registered bean: {}", loadedClass.getName());
            });
  }

  private Loaded<?> createClassForEntity(String className, Class<? extends TenantableEntity> te) {
    val unloadedSubclass =
        new ByteBuddy()
            .subclass(Builder.parameterizedType(R2dbcSaveCallbackSetTenantIdTemplate.class, te).build())
            .annotateType(AnnotationDescription.Builder.ofType(Component.class).build())
            .name(className)
            .make();

    return unloadedSubclass.load(getClass().getClassLoader(), Default.INJECTION);
  }

  private Set<Class<? extends TenantableEntity>> tenantableEntities() {
    val reflections = new Reflections("ch.andrewrembrandt.effectiveit");
    return reflections.getSubTypesOf(TenantableEntity.class);
  }
}
