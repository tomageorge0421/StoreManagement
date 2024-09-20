package Dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;

import static java.lang.Integer.parseInt;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 * @Source http://www.java-blog.com/mapping-javaobjects-database-reflection-generics
 */

/**
 * This class provides an abstract implementation of a Data Access Object (DAO) for database entities.
 * It supports basic CRUD (Create, Read, Update, Delete) operations using reflection and JDBC.
 *
 * @param <T> The type of entity managed by this DAO.
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructs a new AbstractDAO instance, determining the type of entity managed by this DAO.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * Creates a SELECT query to retrieve rows of the entity type from the database based on a field value.
     *
     * @param field The field to filter by.
     * @return The SQL SELECT query.
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" =?");
        return sb.toString();
    }

    /**
     * * Creates a SELECT query to retrieve all rows of the entity type from the database.
     *
     * @return The SQL SELECT query.
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return A list of all entities found, or null if an error occurred.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Retrieves an entity of type T by its ID from the database.
     *
     * @param id The ID of the entity to retrieve.
     * @return The entity with the specified ID, or null if not found.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> result = createObjects(resultSet);
            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creates objects of type T from a ResultSet obtained from the database.
     *
     * @param resultSet The ResultSet containing data retrieved from the database.
     * @return A list of objects of type T created from the ResultSet.
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IntrospectionException | InvocationTargetException | IllegalAccessException |
                 SecurityException | IllegalArgumentException | SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts a new entity into the database.
     *
     * @param t The entity to insert.
     * @return The inserted entity, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while inserting the entity.
     */
    public T insert(T t) throws IllegalAccessException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT ");
        sb.append(" INTO ");
        sb.append(type.getSimpleName());
        sb.append(" VALUES (");
        int ctr = 0;
        Field[] f2 = t.getClass().getDeclaredFields();
        for (Field field : f2) {
            field.setAccessible(true);
            Object a = new Object();
            a = field.get(t);
            try {
                if (ctr == 0) {
                    ctr++;
                    if (a.getClass().getSimpleName().equals("String")) {
                        sb.append(" ' ");
                        sb.append(String.valueOf(field.get(t)));
                        sb.append(" ' ");
                    } else {
                        sb.append(String.valueOf(field.get(t)));
                    }
                } else {
                    sb.append(" , ");
                    if (a.getClass().getSimpleName().equals("String")) {
                        sb.append(" ' ");
                        sb.append(String.valueOf(field.get(t)));
                        sb.append(" ' ");
                    } else {
                        sb.append(String.valueOf(field.get(t)));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sb.append(")");
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    /**
     * Updates an existing entity in the database.
     *
     * @param t The entity to update.
     * @return The updated entity, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while updating the entity.
     */
    public T update(T t) throws IllegalAccessException {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        int ctr = 0;
        int id = 0;
        Field[] f2 = t.getClass().getDeclaredFields();
        for (Field field : f2) {
            field.setAccessible(true);
            Object a = new Object();
            a = field.get(t);
            if (field.getName().equals("id")) id = parseInt(String.valueOf(field.get(t)));
            else {
                try {
                    if (ctr == 0) {
                        ctr++;
                        if (a.getClass().getSimpleName().equals("String")) {
                            sb.append(field.getName());
                            sb.append(" = ");
                            sb.append(" ' ");
                            sb.append(String.valueOf(field.get(t)));
                            sb.append(" ' ");
                        } else {
                            sb.append(field.getName());
                            sb.append(" = ");
                            sb.append(String.valueOf(field.get(t)));
                        }
                    } else {
                        sb.append(" , ");
                        if (a.getClass().getSimpleName().equals("String")) {
                            sb.append(field.getName());
                            sb.append(" = ");
                            sb.append(" ' ");
                            sb.append(String.valueOf(field.get(t)));
                            sb.append(" ' ");
                        } else {
                            sb.append(field.getName());
                            sb.append(" = ");
                            sb.append(String.valueOf(field.get(t)));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sb.append(" WHERE id = ");
        sb.append(id);
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id The ID of the entity to delete.
     * @param t  The entity to delete.
     * @return The deleted entity, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while deleting the entity.
     */
    public T delete(int id, T t) throws IllegalAccessException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        sb.append(" id = ");
        sb.append(id);
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
