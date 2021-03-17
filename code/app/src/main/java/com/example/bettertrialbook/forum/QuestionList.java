package com.example.bettertrialbook.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.Callback;
import com.example.bettertrialbook.models.Question;
import com.example.bettertrialbook.models.Reply;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Displays a list of questions, expands them to show replies and reply button when tapped.
 */
public class QuestionList extends ArrayAdapter<Question> {
   private Context context;
   private Callback<Question> postReply;
   private Set<String> expandedQuestionIds;
    // Maps reply id to it's view (if one has been created)
    // Every time we redraw the list, we hide replies for collapsed questions and make replies visible
    // for any expanded questions.
    // We only actually create the reply views the first time a question is opened.
   private Map<String, View> replyViews;


   public QuestionList(@NonNull Context context, Callback<Question> postReply) {
      super(context, 0);
      this.context = context;
      this.postReply = postReply;
      expandedQuestionIds = new HashSet<>();
      replyViews = new Hashtable<>();
   }


   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = convertView;

       if (view == null){
          view = LayoutInflater.from(getContext()).inflate(R.layout.question_list, parent, false);
       }

       Question question = this.getItem(position);
       TextView title = view.findViewById(R.id.q_title);
       TextView replyCount = view.findViewById(R.id.q_reply_count);
       TextView poster = view.findViewById(R.id.q_poster);


       title.setText(question.getTitle());
       replyCount.setText(question.getReplies().size() + " Response(s)");
       poster.setText("By: " + question.getPosterId().substring(0, 4));

       title.setOnClickListener(titleView -> toggleExpandQuestion(question));
       drawExpandedQuestion(view, question);
       return view;
   }

   private void toggleExpandQuestion(Question question) {
       // collapse the question if it was already opened
       String qid = question.getId();
       if (expandedQuestionIds.contains(qid)) {
           expandedQuestionIds.remove(qid);
       } else expandedQuestionIds.add(qid);
       this.notifyDataSetChanged();
   }

   private boolean questionIsExpanded(Question q) {
       return expandedQuestionIds.contains(q.getId());
   }

    /**
     * Checks if the question should be expanded and sets the visibility of the question's expanded view to match.
     * Populates replies for expanded questions
     * @param view
     * @param q
     */
   private void drawExpandedQuestion(View view, Question q) {
       // we clear old replies so that we don't get duplicates
       hideRepliesToQuestion(q);
       LinearLayout expanded = view.findViewById(R.id.q_expanded);
       if (!questionIsExpanded(q)) {
           if (expanded != null)
           expanded.setVisibility(View.GONE);
           return;
       }
       expanded.setVisibility(View.VISIBLE);
       TextView body = view.findViewById(R.id.q_body);
       body.setText(q.getText());
       Button replyButton = view.findViewById(R.id.post_reply);
       replyButton.setOnClickListener(v -> postReply.execute(q));

       for (Reply r : q.getReplies())
           drawReply(expanded, r);
   }

    /**
     * Inflates a new view for the given reply, or makes an existing reply view visible.
     * @param expandedView The view group containing everything shown when question is expanded
     * @param r the reply to show
     */
   private void drawReply(ViewGroup expandedView, Reply r){
       View replyView = replyViews.get(r.getId());
       if (replyView == null){
            replyView = View.inflate(getContext(), R.layout.reply, expandedView);
       }
       replyView.setVisibility(View.VISIBLE);
       TextView replyBody = replyView.findViewById(R.id.reply_body);
       TextView replyPoster = replyView.findViewById(R.id.reply_poster);

       replyBody.setText(r.getText());
       replyPoster.setText("By: " + r.getPosterId().substring(0, 4));
       // add an entry so that this view gets removed when we need to redraw or hide this question.
       replyViews.put(r.getId(), replyView);
   }

    /**
     * Hides all views that correspond to a reply to this question.
     * @param q
     */
   private void hideRepliesToQuestion(Question q) {
       for (Reply r : q.getReplies()) {
           View replyView = replyViews.get(r.getId());
           // no view has been created for this reply
           if (replyView == null)
               continue;
           replyView.setVisibility(View.GONE);
       }
   }

}
