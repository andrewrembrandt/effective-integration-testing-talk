package ch.andrewrembrandt.effectiveit;

import ch.andrewrembrandt.effectiveit.tenantaware.TenantContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BaseTenantAwareTest {
  default <T> Mono<T> setDefaultTenant(Mono<T> source) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, 1l));
  }
  default <T> Flux<T> setDefaultTenant(Flux<T> source) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, 1l));
  }
  default <T> Mono<T> setEmptyTenant(Mono<T> source) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, 3l));
  }
  default <T> Flux<T> setEmptyTenant(Flux<T> source) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, 3l));
  }
  default <T> Mono<T> addTenant(Mono<T> source, Long tenantId) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, tenantId));
  }

  default <T> Flux<T> addTenant(Flux<T> source, Long tenantId) {
    return source.contextWrite(ctx -> ctx.put(TenantContext.TENANT_ID_KEY, tenantId));
  }
}
