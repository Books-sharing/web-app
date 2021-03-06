package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QueueDao;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.util.db.SpecifiedElementFinder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DBQueueDao implements QueueDao
{
    private enum DBColumnName {ORDER_DATE}

    private final SessionFactory sessionFactory;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBQueueDao(SessionFactory sessionFactory,
                      @Qualifier(value = "streamFinder") SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<Queue> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.Queue q", Queue.class).stream();
    }

    @Override
    public Optional<Queue> getById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return Optional.ofNullable(currentSession.get(Queue.class, id));
    }

    @Override
    public Stream<Queue> getByOrderDate(LocalDateTime orderDate) {
        return specifiedElementFinder.findSpecifiedElementInDB(orderDate, DBColumnName.ORDER_DATE, Queue.class);
    }

    @Override
    public void add(Queue queue) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(queue);
    }

    @Override
    public void update(Queue queue) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(queue);
    }

    @Override
    public void delete(Queue queue) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(queue);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
