package com.example.bettertrialbook.forum;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Question;

import java.util.HashSet;

/**
 * Displays a list of questions, expands them to show replies and reply button when tapped.
 */
public class QuestionList extends ArrayAdapter<Question> {
   private Context context;
   private HashSet<String> expandedQuestionIds;


   public QuestionList(@NonNull Context context) {
      super(context, 0);
      this.context = context;
      expandedQuestionIds = new HashSet<>();
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
       poster.setText("By: " + question.getPosterId());

       title.setOnClickListener(titleView -> toggleExpandQuestion(question));
       drawExpandedQuestion(view, question);
       return view;
   }

   private void toggleExpandQuestion(Question question) {
       // collapse the question if it was already opened
       String qid = question.getId();
       if (expandedQuestionIds.contains(qid))
           expandedQuestionIds.remove(qid);
       else expandedQuestionIds.add(qid);
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
       View expanded = view.findViewById(R.id.q_expanded);
       if (!questionIsExpanded(q)) {
           expanded.setVisibility(View.GONE);
           return;
       }
       expanded.setVisibility(View.VISIBLE);
       TextView body = view.findViewById(R.id.q_body);
       body.setText(q.getText());
   }


}
