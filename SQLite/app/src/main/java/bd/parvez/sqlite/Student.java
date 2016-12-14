package bd.parvez.sqlite;

import java.io.Serializable;

/**
 * Created by ParveZ on 15-Nov-15.
 */
public class Student implements Serializable {
    private String name, id, institution, department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return name+"\n"+id+"\n"+department;
    }
}
