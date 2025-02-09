package io.jenkins.plugins.todeclarative.converter.jobproperty;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import io.jenkins.plugins.todeclarative.converter.api.ConverterRequest;
import io.jenkins.plugins.todeclarative.converter.api.ConverterResult;
import io.jenkins.plugins.todeclarative.converter.api.jobproperty.JobPropertyConverter;
import org.apache.commons.lang3.StringUtils;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTMethodArg;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTOption;

import java.util.ArrayList;
import java.util.List;

import static io.jenkins.plugins.todeclarative.converter.api.ModelASTUtils.addOption;
import static io.jenkins.plugins.todeclarative.converter.api.ModelASTUtils.buildKeyPairArg;

@Extension(optional = true)
public class RequiredResourcesPropertyConverter
    implements JobPropertyConverter
{
    @Override
    public void convert( ConverterRequest request, ConverterResult converterResult, //
                         JobPropertyDescriptor jobPropertyDescriptor, //
                         JobProperty jobProperty )
    {

        org.jenkins.plugins.lockableresources.RequiredResourcesProperty requiredResourcesProperty =
            (org.jenkins.plugins.lockableresources.RequiredResourcesProperty) jobProperty;

        //lock(label: 'label', resource: 'resource')
        ModelASTOption option = new ModelASTOption( this );
        option.setName( "lock" );

        List<ModelASTMethodArg> lockArgs = new ArrayList<>();

        if( StringUtils.isNotBlank(requiredResourcesProperty.getLabelName())){
            lockArgs.add( buildKeyPairArg("label", requiredResourcesProperty.getLabelName()));
        }

        if( StringUtils.isNotBlank(requiredResourcesProperty.getResourceNames())){
            lockArgs.add( buildKeyPairArg("resource", requiredResourcesProperty.getResourceNames()));
        }

        option.setArgs( lockArgs );

        addOption(converterResult.getModelASTPipelineDef(), option );
    }

    @Override
    public boolean canConvert( JobPropertyDescriptor jobPropertyDescriptor, JobProperty jobProperty )
    {
        return jobProperty.getClass().isAssignableFrom( org.jenkins.plugins.lockableresources.RequiredResourcesProperty.class );
    }
}
