package com.chiemy.lib.router;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

public class NBRouter {
    private Map<String, String> routeInfo;

    private NBRouter(){
    }

    public static NBRouter instance() {
        return Holder.sRouter;
    }

    private static class Holder {
        static NBRouter sRouter = new NBRouter();
    }

    public void navigate(Context context, String path) {
        String className = routeInfo.get(path);
        if (className != null) {
            try {
                context.startActivity(new Intent(context, Class.forName(className)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        try {
            Class routes = Class.forName("com.chiemy.lib.router.Routes");
            IRoutes iRoutes = (IRoutes) routes.newInstance();
            routeInfo = iRoutes.getRouteInfo();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
