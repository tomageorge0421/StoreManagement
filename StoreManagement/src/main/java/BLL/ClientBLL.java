package BLL;

import Dao.ClientDAO;
import Model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides business logic operations for managing clients.
 * It acts as an intermediary between the presentation layer and the data access layer.
 */

public class ClientBLL {
    private ClientDAO clientDAO;

    /**
     * Constructs a new ClientBLL instance and initializes the associated DAO.
     */
    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    /**
     * Retrieves a client by its ID from the database.
     *
     * @param id The ID of the client to retrieve.
     * @return The client with the specified ID, or null if not found.
     */
    public Client findClientById(int id) {
        Client cl = clientDAO.findById(id);
        if (cl == null) {
            return null;
        }
        return cl;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return A list containing all clients found in the database.
     */
    public List<Client> findAll() {
        List<Client> c = new ArrayList<>();
        c = clientDAO.findAll();
        return c;
    }

    /**
     * Inserts a new client into the database.
     *
     * @param client The client to insert.
     * @return The inserted client, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while inserting the client.
     */
    public Client insert(Client client) throws IllegalAccessException {
        Client c = clientDAO.insert(client);
        return c;
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client The client to update.
     * @return The updated client, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while updating the client.
     */
    public Client update(Client client) throws IllegalAccessException {
        Client c = clientDAO.update(client);
        return c;
    }

    /**
     * Deletes a client from the database by its ID.
     *
     * @param id     The ID of the client to delete.
     * @param client The client to delete.
     * @return The deleted client, or null if an error occurred.
     * @throws IllegalAccessException If an illegal access operation occurs while deleting the client.
     */
    public Client delete(int id, Client client) throws IllegalAccessException {
        Client c = clientDAO.delete(id, client);
        return c;
    }
}
