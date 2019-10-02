package org.apache.maven.xml.filter;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Objects;

/**
 * 
 * @author Robert Scholte
 * @since 3.7.0
 */
public class DependencyKey
{
    private final String groupId;
    
    private final String artifactId;
    
    private final int hashCode;

    public DependencyKey( String groupId, String artifactId )
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        
        this.hashCode = Objects.hash( artifactId, groupId );
    }
 
    public String getGroupId()
    {
        return groupId;
    }
    
    public String getArtifactId()
    {
        return artifactId;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj ) 
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        
        DependencyKey other = (DependencyKey) obj;
        
        if ( !Objects.equals( artifactId, other.artifactId ) )
        {
            return false;
        }
        if ( !Objects.equals( groupId, other.groupId ) )
        {
            return false;
        }
        
        return true;
    }
    
    
}
