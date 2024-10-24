/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package pof;

import com.tangosol.io.pof.schema.annotation.Portable;
import com.tangosol.io.pof.schema.annotation.PortableType;

import java.util.Objects;

/**
 * A test class annotated with POF annotations.
 *
 * @author Jonathan Knight  2020.07.02
 */
@PortableType
public class Person
    {
    @Portable
    private String firstName;

    @Portable
    private String lastName;


    public Person(String firstName, String lastName)
        {
        this.firstName = firstName;
        this.lastName = lastName;
        }

    public String getFirstName()
        {
        return firstName;
        }

    public void setFirstName(String firstName)
        {
        this.firstName = firstName;
        }

    public String getLastName()
        {
        return lastName;
        }

    public void setLastName(String lastName)
        {
        this.lastName = lastName;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(firstName, lastName);
        }

    @Override
    public String toString()
        {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
        }
    }
