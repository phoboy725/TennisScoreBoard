package com.tennis.filter;

import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter(value = "/*")
public class EntityManagerFilter implements Filter {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Tennis");

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityManagerUtil.bindEntityManager(em);
            chain.doFilter(request, response);
        } finally {
            EntityManagerUtil.unbindEntityManager();
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}