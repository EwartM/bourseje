/*
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.bourseje.client.application.vote;

import java.util.List;

import com.arcbees.bourseje.client.NameTokens;
import com.arcbees.bourseje.client.RestCallbackImpl;
import com.arcbees.bourseje.client.api.CandidateService;
import com.arcbees.bourseje.client.application.ApplicationPresenter;
import com.arcbees.bourseje.shared.Candidate;
import com.arcbees.bourseje.shared.CookieNames;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class VotePresenter extends Presenter<VotePresenter.MyView, VotePresenter.MyProxy> implements VoteUiHandlers {
    interface MyView extends View, HasUiHandlers<VoteUiHandlers> {
        void setCandidatesElement(List<Candidate> candidateList);
    }

    @ProxyStandard
    @NameToken(NameTokens.VOTE)
    interface MyProxy extends ProxyPlace<VotePresenter> {
    }

    private final PlaceManager placeManager;

    private CandidateService candidateService;
    private RestDispatch dispatch;

    @Inject
    VotePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            CandidateService candidateService,
            RestDispatch dispatch) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

        this.placeManager = placeManager;
        this.candidateService = candidateService;
        this.dispatch = dispatch;

        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        dispatch.execute(candidateService.getCandidates(), new RestCallbackImpl<List<Candidate>>() {
            @Override
            public void onSuccess(List<Candidate> result) {
                getView().setCandidatesElement(result);
            }
        });
    }

    @Override
    public void prepareFromRequest(final PlaceRequest request) {
        if (Cookies.getCookie(CookieNames.VOTE_CODE) == null) {
            PlaceRequest placeRequest = new PlaceRequest.Builder()
                    .nameToken(NameTokens.IDENTIFICATION)
                    .build();

            placeManager.revealPlace(placeRequest);
        }
    }

    @Override
    public void onVoteClicked(String name, String company) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.CONFIRM_VOTE)
                .with(NameTokens.PARAM_NAME, name)
                .build();

        placeManager.revealPlace(place);
    }
}
