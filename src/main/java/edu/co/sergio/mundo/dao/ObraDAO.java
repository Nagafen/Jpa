/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.co.sergio.mundo.dao;

import edu.co.sergio.mundo.dao.exceptions.NonexistentEntityException;
import edu.co.sergio.mundo.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.co.sergio.mundo.vo.Artista;
import edu.co.sergio.mundo.vo.Obra;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos
 */
public class ObraDAO implements Serializable {

    public ObraDAO() {
    }
    private EntityManagerFactory emf = null;
    private EntityManager em=null;
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Obra obra) throws PreexistingEntityException, Exception {
        startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista nombreautor = obra.getNombreautor();
            if (nombreautor != null) {
                nombreautor = em.getReference(nombreautor.getClass(), nombreautor.getNombreautor());
                obra.setNombreautor(nombreautor);
            }
            em.persist(obra);
            if (nombreautor != null) {
                nombreautor.getObraCollection().add(obra);
                nombreautor = em.merge(nombreautor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObra(obra.getNombreobra()) != null) {
                throw new PreexistingEntityException("Obra " + obra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                emf.close();
                em.close();
            }
        }
    }

    public void edit(Obra obra) throws NonexistentEntityException, Exception {
        startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Obra persistentObra = em.find(Obra.class, obra.getNombreobra());
            Artista nombreautorOld = persistentObra.getNombreautor();
            Artista nombreautorNew = obra.getNombreautor();
            if (nombreautorNew != null) {
                nombreautorNew = em.getReference(nombreautorNew.getClass(), nombreautorNew.getNombreautor());
                obra.setNombreautor(nombreautorNew);
            }
            obra = em.merge(obra);
            if (nombreautorOld != null && !nombreautorOld.equals(nombreautorNew)) {
                nombreautorOld.getObraCollection().remove(obra);
                nombreautorOld = em.merge(nombreautorOld);
            }
            if (nombreautorNew != null && !nombreautorNew.equals(nombreautorOld)) {
                nombreautorNew.getObraCollection().add(obra);
                nombreautorNew = em.merge(nombreautorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = obra.getNombreobra();
                if (findObra(id) == null) {
                    throw new NonexistentEntityException("The obra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                emf.close();
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Obra obra;
            try {
                obra = em.getReference(Obra.class, id);
                obra.getNombreobra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obra with id " + id + " no longer exists.", enfe);
            }
            Artista nombreautor = obra.getNombreautor();
            if (nombreautor != null) {
                nombreautor.getObraCollection().remove(obra);
                nombreautor = em.merge(nombreautor);
            }
            em.remove(obra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                emf.close();
                em.close();
            }
        }
    }

    public List<Obra> findObraEntities() {
        return findObraEntities(true, -1, -1);
    }

    public List<Obra> findObraEntities(int maxResults, int firstResult) {
        return findObraEntities(false, maxResults, firstResult);
    }

    private List<Obra> findObraEntities(boolean all, int maxResults, int firstResult) {
        startOperation();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Obra.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            emf.close();
            em.close();
        }
    }

    public Obra findObra(String id) {
        startOperation();
        try {
            return em.find(Obra.class, id);
        } finally {
            emf.close();
            em.close();
        }
    }

    public int getObraCount() {
        startOperation();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obra> rt = cq.from(Obra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            emf.close();
            em.close();
        }
    }
    protected void startOperation() { 
        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL")); 
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.url", dbUrl);
            properties.put("javax.persistence.jdbc.user", username );
            properties.put("javax.persistence.jdbc.password", password );
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            this.emf = Persistence.createEntityManagerFactory("catalogPU",properties);
            this.em = emf.createEntityManager();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ObraDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
}
