package net.praqma.jenkins.clearcaseucm.strategies.selectors;

import hudson.FilePath;
import net.praqma.clearcase.exceptions.UnableToInitializeEntityException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.model.BaselineSelector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 21:12
 */
public class NoStreamSelector extends BaselineSelector {

    public NoStreamSelector( FilePath workspace, Component component, Stream stream, Project.PromotionLevel level ) {
        super( workspace, component, stream, level );
    }

    @Override
    public List<Baseline> getValidBaselines() throws IOException, InterruptedException {
        try {
            Baseline b = Baseline.get( "test@\\PVOB" );
            Class bclass = Baseline.class.getSuperclass();
            Field field = bclass.getDeclaredField( "date" );
            field.setAccessible( true );
            field.set( b, new Date() );

            Field field2 = bclass.getDeclaredField( "loaded" );
            field2.setAccessible( true );
            field2.set( b, true );

            return Collections.singletonList( b );
        } catch( UnableToInitializeEntityException e ) {
            e.printStackTrace();
        } catch( NoSuchFieldException e ) {
            e.printStackTrace();
        } catch( IllegalAccessException e ) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


}
