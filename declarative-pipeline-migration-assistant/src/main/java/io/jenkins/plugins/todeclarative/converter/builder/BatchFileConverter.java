package io.jenkins.plugins.todeclarative.converter.builder;

import hudson.Extension;
import hudson.tasks.BatchFile;
import hudson.tasks.Builder;
import io.jenkins.plugins.todeclarative.converter.api.ConverterRequest;
import io.jenkins.plugins.todeclarative.converter.api.ConverterResult;
import io.jenkins.plugins.todeclarative.converter.api.builder.BuilderConverter;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTBranch;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTSingleArgument;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTStage;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTStep;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTValue;

import java.util.Arrays;


@Extension
public class BatchFileConverter
    implements BuilderConverter
{
    public static final String BATCH_NUMBER_KEY = BatchFileConverter.class.getName() + ".shell.number";

    public ModelASTStage convert( ConverterRequest request, ConverterResult converterResult, Builder builder )
    {
        BatchFile batchFile = (BatchFile) builder;
        ModelASTStage stage = new ModelASTStage( this );
        int stageNumber = request.getAndIncrement( BATCH_NUMBER_KEY );
        stage.setName( "Batch script " + stageNumber );
        ModelASTBranch branch = new ModelASTBranch( this );
        stage.setBranches( Arrays.asList( branch ) );
        ModelASTStep step = new ModelASTStep( this );
        ModelASTSingleArgument singleArgument = new ModelASTSingleArgument( this );
        singleArgument.setValue( ModelASTValue.fromConstant( batchFile.getCommand(), this ) );
        step.setArgs( singleArgument );
        step.setName( "bat" );
        wrapBranch(converterResult, step, branch);
        return stage;
    }

    @Override
    public boolean canConvert( Builder builder )
    {
        return builder.getClass().isAssignableFrom( BatchFile.class );
    }
}
