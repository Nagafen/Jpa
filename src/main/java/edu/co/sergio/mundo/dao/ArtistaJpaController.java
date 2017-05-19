/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.co.sergio.mundo.dao;

import edu.co.sergio.mundo.dao.exceptions.NonexistentEntityException;
import edu.co.sergio.mundo.dao.exceptions.PreexistingEntityException;
import edu.co.sergio.mundo.vo.Artista;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.co.sergio.mundo.vo.Obra;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Carlos
 */
public class ArtistaJpaController implements Serializable {

    public ArtistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Artista artista) throws PreexistingEntityException, Exception {
        if (artista.getObraCollection() == null) {
            artista.setObraCollection(new ArrayList<Obra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Obra> attachedObraCollection = new ArrayList<Obra>();
            for (Obra obraCollectionObraToAttach : artista.getObraCollection()) {
                obraCollectionObraToAttach = em.getReference(obraCollectionObraToAttach.getClass(), obraCollectionObraToAttach.getNombreobra());
                attachedObraCollection.add(obraCollectionObraToAttach);
            }
            artista.setObraCollection(attachedObraCollection);
            em.persist(artista);
            for (Obra obraCollectionObra : artista.getObraCollection()) {
                Artista oldNombreautorOfObraCollectionObra = obraCollectionObra.getNombreautor();
                obraCollectionObra.setNombreautor(artista);
                obraCollectionObra = em.merge(obraCollectionObra);
                if (oldNombreautorOfObraCollectionObra != null) {
                    oldNombreautorOfObraCollectionObra.getObraCollection().remove(obraCollectionObra);
                    oldNombreautorOfObraCollectionObra = em.merge(oldNombreautorOfObraCollectionObra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArtista(artista.getNombreautor()) != null) {
                throw new PreexistingEntityException("Artista " + artista + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Artista artista) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista persistentArtista = em.find(Artista.class, artista.getNombreautor());
            Collection<Obra> obraCollectionOld = persistentArtista.getObraCollection();
            Collection<Obra> obraCollectionNew = artista.getObraCollection();
            Collection<Obra> attachedObraCollectionNew = new ArrayList<Obra>();
            for (Obra obraCollectionNewObraToAttach : obraCollectionNew) {
                obraCollectionNewObraToAttach = em.getReference(obraCollectionNewObraToAttach.getClass(), obraCollectionNewObraToAttach.getNombreobra());
                attachedObraCollectionNew.add(obraCollectionNewObraToAttach);
            }
            obraCollectionNew = attachedObraCollectionNew;
            artista.setObraCollection(obraCollectionNew);
            artista = em.merge(artista);
            for (Obra obraCollectionOldObra : obraCollectionOld) {
                if (!obraCollectionNew.contains(obraCollectionOldObra)) {
                    obraCollectionOldObra.setNombreautor(null);
                    obraCollectionOldObra = em.merge(obraCollectionOldObra);
                }
            }
            for (Obra obraCollectionNewObra : obraCollectionNew) {
                if (!obraCollectionOld.contains(obraCollectionNewObra)) {
                    Artista oldNombreautorOfObraCollectionNewObra = obraCollectionNewObra.getNombreautor();
                    obraCollectionNewObra.setNombreautor(artista);
                    obraCollectionNewObra = em.merge(obraCollectionNewObra);
                    if (oldNombreautorOfObraCollectionNewObra != null && !oldNombreautorOfObraCollectionNewObra.equals(artista)) {
                        oldNombreautorOfObraCollectionNewObra.getObraCollection().remove(obraCollectionNewObra);
                        oldNombreautorOfObraCollectionNewObra = em.merge(oldNombreautorOfObraCollectionNewObra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = artista.getNombreautor();
                if (findArtista(id) == null) {
                    throw new NonexistentEntityException("The artista with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista artista;
            try {
                artista = em.getReference(Artista.class, id);
                artista.getNombreautor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The artista with id " + id + " no longer exists.", enfe);
            }
            Collection<Obra> obraCollection = artista.getObraCollection();
            for (Obra obraCollectionObra : obraCollection) {
                obraCollectionObra.setNombreautor(null);
                obraCollectionObra = em.merge(obraCollectionObra);
            }
            em.remove(artista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Artista> findArtistaEntities() {
        return findArtistaEntities(true, -1, -1);
    }

    public List<Artista> findArtistaEntities(int maxResults, int firstResult) {
        return findArtistaEntities(false, maxResults, firstResult);
    }

    private List<Artista> findArtistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Artista.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Artista findArtista(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Artista.class, id);
        } finally {
            em.close();
        }
    }

    public int getArtistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Artista> rt = cq.from(Artista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
