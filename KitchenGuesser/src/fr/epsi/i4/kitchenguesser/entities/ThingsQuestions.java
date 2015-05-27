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
@Table(name = "things_questions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThingsQuestions.findAll", query = "SELECT t FROM ThingsQuestions t"),
    @NamedQuery(name = "ThingsQuestions.findById", query = "SELECT t FROM ThingsQuestions t WHERE t.id = :id"),
    @NamedQuery(name = "ThingsQuestions.findByThingId", query = "SELECT t FROM ThingsQuestions t WHERE t.thingId = :thingId"),
    @NamedQuery(name = "ThingsQuestions.findByQuestionId", query = "SELECT t FROM ThingsQuestions t WHERE t.questionId = :questionId"),
    @NamedQuery(name = "ThingsQuestions.findByValue", query = "SELECT t FROM ThingsQuestions t WHERE t.value = :value")})
public class ThingsQuestions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "thing_id")
    private int thingId;
    @Basic(optional = false)
    @Column(name = "question_id")
    private int questionId;
    @Basic(optional = false)
    @Column(name = "value")
    private int value;

    public ThingsQuestions() {
    }

    public ThingsQuestions(Integer id) {
        this.id = id;
    }

    public ThingsQuestions(Integer id, int thingId, int questionId, int value) {
        this.id = id;
        this.thingId = thingId;
        this.questionId = questionId;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        if (!(object instanceof ThingsQuestions)) {
            return false;
        }
        ThingsQuestions other = (ThingsQuestions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.epsi.i4.kitchenguesser.ThingsQuestions[ id=" + id + " ]";
    }
    
}
