package com.tennis.util;

import jakarta.persistence.EntityManager;

public class EntityManagerUtil {

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_THREAD_LOCAL = new ThreadLocal<>();

    public static EntityManager getCurrentEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_THREAD_LOCAL.get();
        if (entityManager == null) {
            throw new IllegalStateException("No EntityManager bound to current thread. " +
                    "Are you inside a request processed by EntityManagerFilter?");
        }
        return entityManager;
    }

    public static void bindEntityManager(EntityManager em) {
        ENTITY_MANAGER_THREAD_LOCAL.set(em);
    }

    public static void unbindEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_THREAD_LOCAL.get();
        if (entityManager != null) {
            ENTITY_MANAGER_THREAD_LOCAL.remove();
        }
    }
}

