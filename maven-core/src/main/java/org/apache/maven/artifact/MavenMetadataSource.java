package org.apache.maven.artifact;

/* ====================================================================
 *   Copyright 2001-2004 The Apache Software Foundation.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * ====================================================================
 */

import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public class MavenMetadataSource
    implements ArtifactMetadataSource
{
    private MavenXpp3Reader reader = new MavenXpp3Reader();

    private ArtifactRepository localRepository;

    private Set remoteRepositories;

    private ArtifactResolver artifactResolver;

    public MavenMetadataSource( Set remoteRepositories,
                                ArtifactRepository localRepository,
                                ArtifactResolver artifactResolver )
    {
        this.localRepository = localRepository;

        this.artifactResolver = artifactResolver;

        this.remoteRepositories = remoteRepositories;
    }

    public Set retrieve( Artifact artifact )
        throws ArtifactMetadataRetrievalException
    {
        Set artifacts;

        Artifact metadataArtifact = new DefaultArtifact( artifact.getGroupId(),
                                                         artifact.getArtifactId(),
                                                         artifact.getVersion(),
                                                         "pom" );

        try
        {
            artifactResolver.resolve( metadataArtifact, remoteRepositories, localRepository );

            Model model = reader.read( new FileReader( metadataArtifact.getFile() ) );

            artifacts = createArtifacts( model, localRepository );
        }
        catch ( ArtifactResolutionException e )
        {
            throw new ArtifactMetadataRetrievalException( "Error while resolving metadata artifact", e );
        }
        catch ( Exception e )
        {
            throw new ArtifactMetadataRetrievalException( "Cannot read artifact source: " + metadataArtifact.getFile(), e );
        }

        return artifacts;
    }

    public Set createArtifacts( Model model, ArtifactRepository localRepository )
    {
        Set projectArtifacts = new HashSet();
        
        Parent parent = model.getParent();
        if(parent != null)
        {
            projectArtifacts.add( new DefaultArtifact( parent.getGroupId(), 
                                                       parent.getArtifactId(), 
                                                       parent.getVersion(), 
                                                       "pom" ) );
        }

        List dependencies = model.getDependencies();
        for ( Iterator i = dependencies.iterator(); i.hasNext(); )
        {
            Dependency d = (Dependency) i.next();

            Artifact artifact = createArtifact( d, localRepository );

            projectArtifacts.add( artifact );
        }

        return projectArtifacts;
    }

    public Artifact createArtifact( Dependency dependency, ArtifactRepository localRepository )
    {
        Artifact artifact = new DefaultArtifact( dependency.getGroupId(),
                                                 dependency.getArtifactId(),
                                                 dependency.getVersion(),
                                                 dependency.getType() );

        return artifact;
    }

}
