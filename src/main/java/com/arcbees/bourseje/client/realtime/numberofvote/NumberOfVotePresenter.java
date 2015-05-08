/**
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

package com.arcbees.bourseje.client.realtime.numberofvote;

import java.util.Collection;

import com.arcbees.bourseje.client.NameTokens;
import com.arcbees.bourseje.client.RestCallbackImpl;
import com.arcbees.bourseje.client.api.VoteService;
import com.arcbees.bourseje.client.model.Candidates;
import com.arcbees.bourseje.client.realtime.RealtimePresenter;
import com.arcbees.bourseje.shared.CandidateResult;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class NumberOfVotePresenter extends Presenter<NumberOfVotePresenter.MyView, NumberOfVotePresenter.MyProxy> {
    interface MyView extends View {
        void setJohanieVotes(int votes);

        void setDominicVotes(int votes);

        void setRaphaelVotes(int votes);

        void setMaximeVotes(int votes);

        void setSimonVotes(int votes);

        void setVincentVotes(int votes);
    }

    @ProxyStandard
    @NameToken(NameTokens.NUMBER_OF_VOTE)
    interface MyProxy extends ProxyPlace<NumberOfVotePresenter> {
    }

    private final RestDispatch dispatch;
    private final VoteService voteService;

    @Inject
    NumberOfVotePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            RestDispatch dispatch,
            VoteService voteService) {
        super(eventBus, view, proxy, RealtimePresenter.SLOT_MAIN);

        this.dispatch = dispatch;
        this.voteService = voteService;
    }

    @Override
    protected void onReveal() {
        dispatch.execute(voteService.getVotesPerCandidate(), new RestCallbackImpl<Collection<CandidateResult>>() {
            @Override
            public void onSuccess(Collection<CandidateResult> result) {
                for (CandidateResult candidateResult : result) {
                    setNumberOfVote(candidateResult);
                }
            }
        });
    }

    private void setNumberOfVote(CandidateResult candidateResult) {
        if (Candidates.JOHANIE.getName().equals(candidateResult.getCandidateName())) {
            getView().setJohanieVotes(candidateResult.getNumberOfVotes());
        } else if (Candidates.DOMINIC.getName().equals(candidateResult.getCandidateName())) {
            getView().setDominicVotes(candidateResult.getNumberOfVotes());
        } else if (Candidates.RAPHAEL.getName().equals(candidateResult.getCandidateName())) {
            getView().setRaphaelVotes(candidateResult.getNumberOfVotes());
        } else if (Candidates.MAXIME.getName().equals(candidateResult.getCandidateName())) {
            getView().setMaximeVotes(candidateResult.getNumberOfVotes());
        } else if (Candidates.SIMON.getName().equals(candidateResult.getCandidateName())) {
            getView().setSimonVotes(candidateResult.getNumberOfVotes());
        } else if (Candidates.VINCENT.getName().equals(candidateResult.getCandidateName())) {
            getView().setVincentVotes(candidateResult.getNumberOfVotes());
        }
    }
}
