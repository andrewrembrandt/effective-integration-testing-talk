package ch.andrewrembrandt.effectiveit.tenantaware;

import reactor.util.context.ContextView;

public class TenantContext {
  public static final String TENANT_ID_KEY = "tenant-id-key";

  public static Long tenantId(ContextView ctx) {
    if (!ctx.hasKey(TENANT_ID_KEY)) {
      return null;
    }
    return ctx.get(TENANT_ID_KEY);
  }
}
