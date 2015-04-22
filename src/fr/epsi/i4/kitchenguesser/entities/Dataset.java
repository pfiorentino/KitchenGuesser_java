/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paul
 */
@Entity
@Table(name = "dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataset.findAll", query = "SELECT d FROM Dataset d"),
    @NamedQuery(name = "Dataset.findById", query = "SELECT d FROM Dataset d WHERE d.id = :id"),
    @NamedQuery(name = "Dataset.findByGrand", query = "SELECT d FROM Dataset d WHERE d.grand = :grand"),
    @NamedQuery(name = "Dataset.findByM\u00e9tal", query = "SELECT d FROM Dataset d WHERE d.m\u00e9tal = :m\u00e9tal"),
    @NamedQuery(name = "Dataset.findByTissus", query = "SELECT d FROM Dataset d WHERE d.tissus = :tissus"),
    @NamedQuery(name = "Dataset.findByPlastique", query = "SELECT d FROM Dataset d WHERE d.plastique = :plastique"),
    @NamedQuery(name = "Dataset.findByBois", query = "SELECT d FROM Dataset d WHERE d.bois = :bois"),
    @NamedQuery(name = "Dataset.findByVerre", query = "SELECT d FROM Dataset d WHERE d.verre = :verre"),
    @NamedQuery(name = "Dataset.findByUstensile", query = "SELECT d FROM Dataset d WHERE d.ustensile = :ustensile"),
    @NamedQuery(name = "Dataset.findByFix\u00e9", query = "SELECT d FROM Dataset d WHERE d.fix\u00e9 = :fix\u00e9"),
    @NamedQuery(name = "Dataset.findByLourd", query = "SELECT d FROM Dataset d WHERE d.lourd = :lourd"),
    @NamedQuery(name = "Dataset.findByR\u00e9cipient", query = "SELECT d FROM Dataset d WHERE d.r\u00e9cipient = :r\u00e9cipient"),
    @NamedQuery(name = "Dataset.findByFerm\u00e9", query = "SELECT d FROM Dataset d WHERE d.ferm\u00e9 = :ferm\u00e9"),
    @NamedQuery(name = "Dataset.findBySertPourManger", query = "SELECT d FROM Dataset d WHERE d.sertPourManger = :sertPourManger"),
    @NamedQuery(name = "Dataset.findByElectroMenager", query = "SELECT d FROM Dataset d WHERE d.electroMenager = :electroMenager"),
    @NamedQuery(name = "Dataset.findByMobilier", query = "SELECT d FROM Dataset d WHERE d.mobilier = :mobilier"),
    @NamedQuery(name = "Dataset.findByPlut\u00f4tRond", query = "SELECT d FROM Dataset d WHERE d.plut\u00f4tRond = :plut\u00f4tRond"),
    @NamedQuery(name = "Dataset.findByJetable", query = "SELECT d FROM Dataset d WHERE d.jetable = :jetable"),
    @NamedQuery(name = "Dataset.findByD\u00e9chirable", query = "SELECT d FROM Dataset d WHERE d.d\u00e9chirable = :d\u00e9chirable"),
    @NamedQuery(name = "Dataset.findByPoign\u00e9e", query = "SELECT d FROM Dataset d WHERE d.poign\u00e9e = :poign\u00e9e"),
    @NamedQuery(name = "Dataset.findByLaveVaisselle", query = "SELECT d FROM Dataset d WHERE d.laveVaisselle = :laveVaisselle"),
    @NamedQuery(name = "Dataset.findByBoutons", query = "SELECT d FROM Dataset d WHERE d.boutons = :boutons"),
    @NamedQuery(name = "Dataset.findByPlat", query = "SELECT d FROM Dataset d WHERE d.plat = :plat"),
    @NamedQuery(name = "Dataset.findByCoupant", query = "SELECT d FROM Dataset d WHERE d.coupant = :coupant"),
    @NamedQuery(name = "Dataset.findBy\u00c9clairage", query = "SELECT d FROM Dataset d WHERE d.\u00e9clairage = :\u00e9clairage"),
    @NamedQuery(name = "Dataset.findByNom", query = "SELECT d FROM Dataset d WHERE d.nom = :nom")})
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "grand")
    private Integer grand;
    @Column(name = "m\u00e9tal")
    private Integer métal;
    @Column(name = "tissus")
    private Integer tissus;
    @Column(name = "plastique")
    private Integer plastique;
    @Column(name = "bois")
    private Integer bois;
    @Column(name = "verre")
    private Integer verre;
    @Column(name = "ustensile")
    private Integer ustensile;
    @Column(name = "fix\u00e9")
    private Integer fixé;
    @Column(name = "lourd")
    private Integer lourd;
    @Column(name = "r\u00e9cipient")
    private Integer récipient;
    @Column(name = "ferm\u00e9")
    private Integer fermé;
    @Column(name = "sert pour manger")
    private Integer sertPourManger;
    @Column(name = "electro-menager")
    private Integer electroMenager;
    @Column(name = "mobilier")
    private Integer mobilier;
    @Column(name = "plut\u00f4t rond")
    private Integer plutôtRond;
    @Column(name = "jetable")
    private Integer jetable;
    @Column(name = "d\u00e9chirable")
    private Integer déchirable;
    @Column(name = "poign\u00e9e")
    private Integer poignée;
    @Column(name = "lave-vaisselle")
    private Integer laveVaisselle;
    @Column(name = "boutons")
    private Integer boutons;
    @Column(name = "plat")
    private Integer plat;
    @Column(name = "coupant")
    private Integer coupant;
    @Column(name = "\u00e9clairage")
    private Integer éclairage;
    @Column(name = "nom")
    private String nom;

    public Dataset() {
    }

    public Dataset(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGrand() {
        return grand;
    }

    public void setGrand(Integer grand) {
        this.grand = grand;
    }

    public Integer getMétal() {
        return métal;
    }

    public void setMétal(Integer métal) {
        this.métal = métal;
    }

    public Integer getTissus() {
        return tissus;
    }

    public void setTissus(Integer tissus) {
        this.tissus = tissus;
    }

    public Integer getPlastique() {
        return plastique;
    }

    public void setPlastique(Integer plastique) {
        this.plastique = plastique;
    }

    public Integer getBois() {
        return bois;
    }

    public void setBois(Integer bois) {
        this.bois = bois;
    }

    public Integer getVerre() {
        return verre;
    }

    public void setVerre(Integer verre) {
        this.verre = verre;
    }

    public Integer getUstensile() {
        return ustensile;
    }

    public void setUstensile(Integer ustensile) {
        this.ustensile = ustensile;
    }

    public Integer getFixé() {
        return fixé;
    }

    public void setFixé(Integer fixé) {
        this.fixé = fixé;
    }

    public Integer getLourd() {
        return lourd;
    }

    public void setLourd(Integer lourd) {
        this.lourd = lourd;
    }

    public Integer getRécipient() {
        return récipient;
    }

    public void setRécipient(Integer récipient) {
        this.récipient = récipient;
    }

    public Integer getFermé() {
        return fermé;
    }

    public void setFermé(Integer fermé) {
        this.fermé = fermé;
    }

    public Integer getSertPourManger() {
        return sertPourManger;
    }

    public void setSertPourManger(Integer sertPourManger) {
        this.sertPourManger = sertPourManger;
    }

    public Integer getElectroMenager() {
        return electroMenager;
    }

    public void setElectroMenager(Integer electroMenager) {
        this.electroMenager = electroMenager;
    }

    public Integer getMobilier() {
        return mobilier;
    }

    public void setMobilier(Integer mobilier) {
        this.mobilier = mobilier;
    }

    public Integer getPlutôtRond() {
        return plutôtRond;
    }

    public void setPlutôtRond(Integer plutôtRond) {
        this.plutôtRond = plutôtRond;
    }

    public Integer getJetable() {
        return jetable;
    }

    public void setJetable(Integer jetable) {
        this.jetable = jetable;
    }

    public Integer getDéchirable() {
        return déchirable;
    }

    public void setDéchirable(Integer déchirable) {
        this.déchirable = déchirable;
    }

    public Integer getPoignée() {
        return poignée;
    }

    public void setPoignée(Integer poignée) {
        this.poignée = poignée;
    }

    public Integer getLaveVaisselle() {
        return laveVaisselle;
    }

    public void setLaveVaisselle(Integer laveVaisselle) {
        this.laveVaisselle = laveVaisselle;
    }

    public Integer getBoutons() {
        return boutons;
    }

    public void setBoutons(Integer boutons) {
        this.boutons = boutons;
    }

    public Integer getPlat() {
        return plat;
    }

    public void setPlat(Integer plat) {
        this.plat = plat;
    }

    public Integer getCoupant() {
        return coupant;
    }

    public void setCoupant(Integer coupant) {
        this.coupant = coupant;
    }

    public Integer getÉclairage() {
        return éclairage;
    }

    public void setÉclairage(Integer éclairage) {
        this.éclairage = éclairage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataset)) {
            return false;
        }
        Dataset other = (Dataset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kitchenguesser.Dataset[ id=" + id + " ]";
    }
    
}
