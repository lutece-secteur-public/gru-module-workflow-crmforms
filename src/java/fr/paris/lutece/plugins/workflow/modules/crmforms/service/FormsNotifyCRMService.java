/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.crmforms.service;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponseStep;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.forms.util.FormsConstants;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;
import fr.paris.lutece.plugins.workflowcore.service.action.ActionService;
import fr.paris.lutece.plugins.workflowcore.service.action.IActionService;
import fr.paris.lutece.plugins.workflowcore.service.provider.InfoMarker;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * FormsNotifyCRMService
 *
 */
public class FormsNotifyCRMService implements INotifyCRMService
{
    // MARKS
    private static final String MARK_ALL_FORMS = "All forms";
    private static final String MARK_URL_FO_RESPONSE = "url_fo_forms_response_detail";
    
    // PARAMETERS
    public static final String PARAMETER_ID_FORM_RESPONSES_FO = "id_response";
    public static final String PARAMETER_PAGE_FORM_RESPONSE = "formsResponse";
    public static final String PARAMETER_VIEW_FORM_RESPONSE_DETAILS_FO = "formResponseView";

    private static final String MARK_FORM_RESPONSE_FO_URL = "forms.marker.provider.url.fo.detail.reponse.description";

    @Inject
    @Named( ActionService.BEAN_SERVICE )
    IActionService _actionService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ReferenceList> getListMarkers( HttpServletRequest request, ITask task )
    {
        Map<String, ReferenceList> mapMarkers = new HashMap<>( );
        ReferenceList referenceListAll = new ReferenceList( );

        Action action = _actionService.findByPrimaryKey( task.getAction( ).getId( ) );

        Workflow workflow = action.getWorkflow( );

        for ( Form form : FormHome.getFormList( ) )
        {
            if ( form.getIdWorkflow( ) == workflow.getId( ) )
            {
                ReferenceList referenceList = new ReferenceList( );
                List<Question> questionList = QuestionHome.getListQuestionByIdForm( form.getId( ) ).stream( ).filter( distinctByKey( Question::getCode ) )
                        .collect( Collectors.toList( ) );

                questionList.stream( ).forEach( q -> referenceList.addItem( q.getCode( ), q.getTitle( ) ) );

                mapMarkers.put( form.getTitle( ), referenceList );
                referenceListAll.addAll( referenceList );
            }
        }

        ReferenceList referenceList = new ReferenceList( );
        referenceList.addItem( MARK_URL_FO_RESPONSE, I18nService.getLocalizedString( MARK_FORM_RESPONSE_FO_URL , I18nService.getDefaultLocale( ) ) );
        
       referenceListAll.addAll( referenceList );

        mapMarkers.put( MARK_ALL_FORMS, referenceListAll );

        return mapMarkers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> fillModelInfoResource( int nIdRessource )
    {
        Map<String, Object> model = new HashMap<>( );
        FormResponse formResponse = FormResponseHome.findByPrimaryKey( nIdRessource );

        for ( FormResponseStep formResponseStep : formResponse.getSteps( ) )
        {
            for ( FormQuestionResponse formQuestionResponse : formResponseStep.getQuestions( ) )
            {
                for ( Response response : formQuestionResponse.getEntryResponse( ) )
                {
                    model.put( formQuestionResponse.getQuestion( ).getCode( ), response.getResponseValue( ) );
                }
            }
        }
        
        Collection<InfoMarker> result = new ArrayList<>( );
        
        InfoMarker notifyMarkerFOUrl = new InfoMarker( MARK_URL_FO_RESPONSE );
        UrlItem urlFO = new UrlItem( AppPathService.getProdUrl( (HttpServletRequest) null ) + AppPathService.getPortalUrl( ) );
        urlFO.addParameter( FormsConstants.PARAMETER_PAGE, PARAMETER_PAGE_FORM_RESPONSE );
        urlFO.addParameter( FormsConstants.PARAMETER_TARGET_VIEW, PARAMETER_VIEW_FORM_RESPONSE_DETAILS_FO );
        urlFO.addParameter( PARAMETER_ID_FORM_RESPONSES_FO, formResponse.getId( ) );
        notifyMarkerFOUrl.setValue( urlFO.getUrl( ) );
        result.add( notifyMarkerFOUrl );
        
        model.put(MARK_URL_FO_RESPONSE, urlFO.toString( ) );
        
        return model;
    }

    /**
     * distinctByKey
     * 
     * @param <T>
     * @param keyExtractor
     * @return
     */
    private static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor )
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>( );
        return t -> map.putIfAbsent( keyExtractor.apply( t ), Boolean.TRUE ) == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdDemand( int nIdRessource )
    {
        return Integer.toString( nIdRessource );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserGuid( int nIdRessource )
    {
        FormResponse formResponse = FormResponseHome.loadById( nIdRessource );
        return formResponse.getGuid( );
    }
}
