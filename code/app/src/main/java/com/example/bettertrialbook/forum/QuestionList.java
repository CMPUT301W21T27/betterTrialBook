package com.example.bettertrialbook.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Question;

import org.w3c.dom.Text;

import java.util.List;

public class QuestionList extends ArrayAdapter<Question> {
   private List<Question> questions;
   private Context context;


   public QuestionList(@NonNull Context context, List<Question> questions) {
      super(context, 0, questions);
      this.questions = questions;
      this.context = context;
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = convertView;

       if (view == null){
          view = LayoutInflater.from(getContext()).inflate(R.layout.question_list, parent, false);
       }

       Question question = questions.get(position);
       TextView title = view.findViewById(R.id.qtitle);

       title.setText(question.getTitle());
       return view;
   }
}
