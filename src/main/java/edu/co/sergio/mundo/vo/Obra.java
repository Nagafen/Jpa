/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.co.sergio.mundo.vo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "obra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obra.findAll", query = "SELECT o FROM Obra o")
    , @NamedQuery(name = "Obra.findByNombreobra", query = "SELECT o FROM Obra o WHERE o.nombreobra = :nombreobra")
    , @NamedQuery(name = "Obra.findByDescripcion", query = "SELECT o FROM Obra o WHERE o.descripcion = :descripcion")
    , @NamedQuery(name = "Obra.findByEstilo", query = "SELECT o FROM Obra o WHERE o.estilo = :estilo")
    , @NamedQuery(name = "Obra.findByValor", query = "SELECT o FROM Obra o WHERE o.valor = :valor")})
public class Obra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreobra")
    private String nombreobra;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "estilo")
    private String estilo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Float valor;
    @JoinColumn(name = "nombreautor", referencedColumnName = "nombreautor")
    @ManyToOne
    private Artista nombreautor;

    public Obra() {
    }

    public Obra(String nombreobra) {
        this.nombreobra = nombreobra;
    }

    public String getNombreobra() {
        return nombreobra;
    }

    public void setNombreobra(String nombreobra) {
        this.nombreobra = nombreobra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Artista getNombreautor() {
        return nombreautor;
    }

    public void setNombreautor(Artista nombreautor) {
        this.nombreautor = nombreautor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreobra != null ? nombreobra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obra)) {
            return false;
        }
        Obra other = (Obra) object;
        if ((this.nombreobra == null && other.nombreobra != null) || (this.nombreobra != null && !this.nombreobra.equals(other.nombreobra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.co.sergio.mundo.vo.Obra[ nombreobra=" + nombreobra + " ]";
    }
    
}
