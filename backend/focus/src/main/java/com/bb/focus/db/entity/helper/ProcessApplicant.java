package com.bb.focus.db.entity.helper;


import com.bb.focus.db.entity.applicant.Applicant;
import com.bb.focus.db.entity.process.Process;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="process_applicants")
public class ProcessApplicant {


    @Id
    @GeneratedValue
    @Column(name="process_applicants_id")
    private int processApplicant;

    @ManyToOne
    @JoinColumn(name="applicant_id")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name="process_id")
    private Process process;




}
