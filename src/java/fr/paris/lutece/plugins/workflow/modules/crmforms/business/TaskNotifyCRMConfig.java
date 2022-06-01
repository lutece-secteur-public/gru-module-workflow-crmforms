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
package fr.paris.lutece.plugins.workflow.modules.crmforms.business;

import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;
import javax.validation.constraints.NotNull;

/**
 *
 * TaskNotifyCRMConfig
 *
 */
public class TaskNotifyCRMConfig extends TaskConfig
{

    @NotNull
    private String _strStatusText;

    private boolean _bDemandCRMCreation;

    @NotNull
    private String _strMessage;

    @NotNull
    private String _strObject;

    @NotNull
    private String _strSender;

    /**
     * Set the object
     * 
     * @param strObject
     *            the object
     */
    public void setObject( String strObject )
    {
        _strObject = strObject;
    }

    /**
     * Get the object
     * 
     * @return the object
     */
    public String getObject( )
    {
        return _strObject;
    }

    /**
     * Set the sender
     * 
     * @param strSender
     *            the sender
     */
    public void setSender( String strSender )
    {
        _strSender = strSender;
    }

    /**
     * Get the sender
     * 
     * @return the sender
     */
    public String getSender( )
    {
        return _strSender;
    }

    /**
     * Set the message
     * 
     * @param strMessage
     *            the message
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
     * Get the message
     * 
     * @return the message
     */
    public String getMessage( )
    {
        return _strMessage;
    }

    /**
     * Set the status
     * 
     * @param strStatusText
     *            the status
     */
    public void setStatusText( String strStatusText )
    {
        _strStatusText = strStatusText;
    }

    /**
     * Get the status
     * 
     * @return the status
     */
    public String getStatusText( )
    {
        return _strStatusText;
    }

    /**
     * Set the DemandCRMCreation boolean
     * 
     * @param bDemandCRMCreation
     *            the boolean to create CRM demand
     */
    public void setDemandCRMCreation( boolean bDemandCRMCreation )
    {
        _bDemandCRMCreation = bDemandCRMCreation;
    }

    /**
     * Get the DemandCRMCreation boolean
     * 
     * @return the DemandCRMCreation boolean
     */
    public boolean getDemandCRMCreation( )
    {
        return _bDemandCRMCreation;
    }
}
