package ch.andrewrembrandt.effectiveit.config;

import ch.andrewrembrandt.effectiveit.model.TenantableEntity;
import ch.andrewrembrandt.effectiveit.tenantaware.TenantContext;
import lombok.val;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import reactor.core.publisher.Mono;

public class R2dbcSaveCallbackSetTenantIdTemplate<T extends TenantableEntity> implements BeforeSaveCallback<T> {
  @Override
  public Mono<T> onBeforeSave(T entity, OutboundRow row, SqlIdentifier table) {
    return Mono.deferContextual(
        ctx -> {
          val tenantId = TenantContext.tenantId(ctx);
          if (tenantId != null) {
            entity.setTenantId(tenantId);
            row.append("tenant_id", Parameter.from(tenantId));
            return Mono.just(entity);
          }
          return Mono.just(entity);
        });
  }
}
