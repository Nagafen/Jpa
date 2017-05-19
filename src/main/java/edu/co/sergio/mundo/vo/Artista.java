/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.co.sergio.mundo.vo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "artista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artista.findAll", query = "SELECT a FROM Artista a")
    , @NamedQuery(name = "Artista.findByUserart", query = "SELECT a FROM Artista a WHERE a.userart = :userart")
    , @NamedQuery(name = "Artista.findByNombreautor", query = "SELECT a FROM Artista a WHERE a.nombreautor = :nombreautor")
    , @NamedQuery(name = "Artista.findByCurriculum", query = "SELECT a FROM Artista a WHERE a.curriculum = :curriculum")
    , @NamedQuery(name = "Artista.findByDistinciones", query = "SELECT a FROM Artista a WHERE a.distinciones = :distinciones")})
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "userart")
    private String userart;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreautor")
    private String nombreautor;
    @Size(max = 2147483647)
    @Column(name = "curriculum")
    private String curriculum;
    @Size(max = 2147483647)
    @Column(name = "distinciones")
    private String distinciones;
    @OneToMany(mappedBy = "nombreautor")
    private Collection<Obra> obraCollection;

    public Artista() {
    }

    public Artista(String nombreautor) {
        this.nombreautor = nombreautor;
    }

    public String getUserart() {
        return userart;
    }

    public void setUserart(String userart) {
        this.userart = userart;
    }

    public String getNombreautor() {
        return nombreautor;
    }

    public void setNombreautor(String nombreautor) {
        this.nombreautor = nombreautor;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getDistinciones() {
        return distinciones;
    }

    public void setDistinciones(String distinciones) {
        this.distinciones = distinciones;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Obra> getObraCollection() {
        return obraCollection;
    }

    public void setObraCollection(Collection<Obra> obraCollection) {
        this.obraCollection = obraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreautor != null ? nombreautor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artista)) {
            return false;
        }
        Artista other = (Artista) object;
        if ((this.nombreautor == null && other.nombreautor != null) || (this.nombreautor != null && !this.nombreautor.equals(other.nombreautor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.co.sergio.mundo.vo.Artista[ nombreautor=" + nombreautor + " ]";
    }
    
}
